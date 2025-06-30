package com.nexora.nexora_crypto_api.controller;



import com.nexora.nexora_crypto_api.model.dto.LoginUserDto;
import com.nexora.nexora_crypto_api.model.dto.RegisterUserDto;
import com.nexora.nexora_crypto_api.model.dto.VerifyUserDto;
import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.response.LoginResponse;
import com.nexora.nexora_crypto_api.service.AuthenticationService;
import com.nexora.nexora_crypto_api.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            authenticationService.signup(registerUserDto);

            logger.info("Signup successful: {}", registerUserDto.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Registration successfully"));
        } catch (Exception e) {
            logger.error("Signup failed: {}, errorMessage: {}", registerUserDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User already used"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws Exception {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

            authenticationService.revokeAllUserTokens(authenticatedUser);
            authenticationService.saveUserToken(authenticatedUser, jwtToken);

            LoginResponse loginResponse = new LoginResponse(jwtToken, refreshToken, jwtService.getExpirationTime());

            logger.info("Login successful: {}", loginUserDto.getEmail());

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            logger.error("Login failed: {}, errorMessage: {}", loginUserDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((LoginResponse) Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            logger.info("Verify successful: {}", verifyUserDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Account verified successfully"));
        } catch (Exception e) {
            logger.warn("Verify failed: {}, errorMessage: {}", verifyUserDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Not authenticate"));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            logger.info("Resend successful: {}", email);
            return ResponseEntity.ok("Verification code sent");
        } catch (Exception e) {
            logger.warn("Resend failed: {}, errorMessage: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}