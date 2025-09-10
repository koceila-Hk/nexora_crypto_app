package com.nexora.nexora_crypto_api.controller;



import com.nexora.nexora_crypto_api.model.dto.LoginUserDto;
import com.nexora.nexora_crypto_api.model.dto.RegisterUserDto;
import com.nexora.nexora_crypto_api.model.dto.VerifyUserDto;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.service.AuthenticationService;
import com.nexora.nexora_crypto_api.security.JwtService;
import com.nexora.nexora_crypto_api.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;

    private static final String MESSAGE = "message";
    private static final String ERROR = "error";

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    /**
     * Register a new user account
     * @param registerUserDto
     * @return success message if registration is ok
     */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            authenticationService.signup(registerUserDto);

            logger.info("Signup successful: {}", registerUserDto.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE, "Registration successfully"));
        } catch (Exception e) {
            logger.error("Signup failed: {}, errorMessage: {}", registerUserDto.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(ERROR, e.getMessage()));
        }
    }

    /**
     *  Auth a user and return jwt and refreshToken as cookies
     * @param loginUserDto
     * @return success message with token in cookies
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String accessToken = jwtService.generateToken(authenticatedUser);
            String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

            ResponseCookie accessCookie = CookieUtil.createAccessTokenCookie(accessToken, jwtService.getExpirationTime());
            ResponseCookie refreshCookie = CookieUtil.createRefreshTokenCookie(refreshToken, jwtService.getRefreshExpirationTime());

            logger.info("Login successful: {}", loginUserDto.getEmail());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
                    .body(Map.of(MESSAGE, "Connexion réussie"));
        } catch (Exception e) {
            logger.error("Login failed: {}, errorMessage: {}", loginUserDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(ERROR, e.getMessage()));
        }
    }

    // Verify user's account with verification code
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            logger.info("Verify successful: {}", verifyUserDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE,"Account verified successfully"));
        } catch (Exception e) {
            logger.warn("Verify failed: {}, errorMessage: {}", verifyUserDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(ERROR, "Not authenticate"));
        }
    }

    // Resend email verification code to the user
    @PostMapping("/resend")
    public ResponseEntity<Map<String, String>> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            logger.info("Resend successful: {}", email);
            return ResponseEntity.ok( Map.of(MESSAGE, "Verification code sent"));
        } catch (Exception e) {
            logger.warn("Resend failed: {}, errorMessage: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(ERROR, e.getMessage()));
        }
    }

    // Refresh accessToken useing refreshToken cookie
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestParam String email) {
        try {
            authenticationService.generateResetToken(email);
            logger.info("Reset password email sent to: {}", email);
            return ResponseEntity.ok(Map.of(MESSAGE, "Email de réinitialisation envoyé"));
        } catch (Exception e) {
            logger.warn("forgot password failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(ERROR, e.getMessage()));
        }
    }

    // reset password using reset token
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("token");
            String newPassword = body.get("newPassword");
            authenticationService.resetPassword(token, newPassword);
            logger.info("password reset successful for token: {}", token);
            return ResponseEntity.ok(Map.of(MESSAGE, "Mot de passe réinitialisé avec succès"));
        } catch (Exception e) {
            logger.warn("password reset failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(ERROR, e.getMessage()));
        }
    }

}





//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws Exception {
//        try {
//            User authenticatedUser = authenticationService.authenticate(loginUserDto);
//            String jwtToken = jwtService.generateToken(authenticatedUser);
//            String refreshToken = jwtService.generateRefreshToken(authenticatedUser);
//
//            authenticationService.revokeAllUserTokens(authenticatedUser);
//            authenticationService.saveUserToken(authenticatedUser, jwtToken);
//
//            LoginResponse loginResponse = new LoginResponse(jwtToken, refreshToken, jwtService.getExpirationTime());
//
//            logger.info("Login successful: {}", loginUserDto.getEmail());
//
//            return ResponseEntity.ok(loginResponse);
//        } catch (Exception e) {
//            logger.error("Login failed: {}, errorMessage: {}", loginUserDto.getEmail(), e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((LoginResponse) Map.of("message", e.getMessage()));
//        }
//    }