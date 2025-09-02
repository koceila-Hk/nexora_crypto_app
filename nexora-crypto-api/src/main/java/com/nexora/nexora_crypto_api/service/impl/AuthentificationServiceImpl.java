package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.Token;
import com.nexora.nexora_crypto_api.model.dto.LoginUserDto;
import com.nexora.nexora_crypto_api.model.dto.RegisterUserDto;
import com.nexora.nexora_crypto_api.model.dto.VerifyUserDto;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.enums.TokenType;
import com.nexora.nexora_crypto_api.repository.TokenRepository;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.service.AuthenticationService;
import com.nexora.nexora_crypto_api.utils.EmailService;
import com.nexora.nexora_crypto_api.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthentificationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenRepository tokenRepository;

    // ------------------- SIGNUP -------------------
    @Override
    public void signup(RegisterUserDto input) throws Exception {
        if (userRepository.existsByEmail(input.getEmail()) || userRepository.existsByPseudonym(input.getPseudonym())) {
            throw new Exception("Utilisateur déjà existant");
        }

        User user = new User();
        user.setEmail(input.getEmail());
        user.setPseudonym(input.getPseudonym());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEnabled(false);
        user.setBalance(BigDecimal.valueOf(1000));
        user.setDateCreation(LocalDateTime.now());

        // Générer code de vérification
        String code = String.valueOf(new SecureRandom().nextInt(900000) + 100000);
        user.setVerificationCode(code);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);
        emailService.sendVerificationEmail(user);
    }

    // ------------------- LOGIN -------------------
    @Override
    public User authenticate(LoginUserDto input) throws Exception {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new Exception("Utilisateur non trouvé"));

        if (!user.isEnabled()) {
            throw new Exception("Compte non vérifié");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        return user;
    }

    // ------------------- VERIFY ACCOUNT -------------------
    @Override
    public void verifyUser(VerifyUserDto input) throws Exception {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new Exception("Utilisateur non trouvé"));

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new Exception("Code expiré");
        }

        if (!user.getVerificationCode().equals(input.getVerificationCode())) {
            throw new Exception("Code invalide");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
    }

    // ------------------- RESEND VERIFICATION CODE -------------------
    @Override
    public void resendVerificationCode(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("Utilisateur non trouvé"));

        if (user.isEnabled()) {
            throw new Exception("Compte déjà vérifié");
        }

        String code = String.valueOf(new SecureRandom().nextInt(900000) + 100000);
        user.setVerificationCode(code);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        emailService.sendVerificationEmail(user);
    }

    // ------------------- REFRESH TOKEN -------------------
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("refresh_token")) refreshToken = c.getValue();
            }
        }

        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Pas de token");
            return;
        }

        String email = jwtService.extractUsername(refreshToken);
        if (email == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Token invalide");
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IOException("Utilisateur non trouvé"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Token invalide");
            return;
        }

        String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        Cookie cookie = new Cookie("access_token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(900); // 15 min
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\":\"Token rafraîchi\"}");
    }

    // ------------------- SAVE TOKEN -------------------
    @Override
    public void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    // ------------------- REVOKE TOKENS -------------------
    @Override
    public void revokeAllUserTokens(User user) {
        var tokens = tokenRepository.findAllValidTokenByUser(user.getId());
        for (Token t : tokens) {
            t.setExpired(true);
            t.setRevoked(true);
        }
        tokenRepository.saveAll(tokens);
    }

    // ------------------- FORGOT PASSWORD -------------------
    @Override
    public void generateResetToken(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("Utilisateur non trouvé"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS));
        userRepository.save(user);

        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    // ------------------- RESET PASSWORD -------------------
    @Override
    public void resetPassword(String token, String newPassword) throws Exception {
        User user = userRepository.findByResetToken(token).orElseThrow(() -> new Exception("Token invalide"));

        if (user.getTokenExpiryDate().isBefore(Instant.now())) {
            throw new Exception("Token expiré");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);
    }


    private String generateVerificationCode() {
        SecureRandom secureRandom = new SecureRandom();
        int code = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
