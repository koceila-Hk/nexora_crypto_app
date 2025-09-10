package com.nexora.nexora_crypto_api.controller;

import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.LoginUserDto;
import com.nexora.nexora_crypto_api.model.dto.RegisterUserDto;
import com.nexora.nexora_crypto_api.model.dto.VerifyUserDto;
import com.nexora.nexora_crypto_api.security.JwtService;
import com.nexora.nexora_crypto_api.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController controller;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldReturnCreatedMessage() throws Exception {
        // Arrange
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail("test@example.com");
        doNothing().when(authenticationService).signup(dto);

        // Act
        ResponseEntity<?> result = controller.register(dto);

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(201);
        assertThat(result.getBody()).isEqualTo(Map.of("message", "Registration successfully"));
        verify(authenticationService, times(1)).signup(dto);
    }

    @Test
    void authenticate_shouldReturnTokensInCookies() throws Exception {
        // Arrange
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        User user = new User();
        user.setId(1L);

        when(authenticationService.authenticate(loginDto)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");

        // Act
        ResponseEntity<?> result = controller.authenticate(loginDto);

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(Map.of("message", "Connexion réussie"));
        verify(authenticationService, times(1)).authenticate(loginDto);
    }

    @Test
    void verifyUser_shouldReturnCreatedMessage() throws Exception {
        // Arrange
        VerifyUserDto verifyDto = new VerifyUserDto();
        verifyDto.setEmail("test@example.com");
        doNothing().when(authenticationService).verifyUser(verifyDto);

        // Act
        ResponseEntity<?> result = controller.verifyUser(verifyDto);

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(201);
        assertThat(result.getBody()).isEqualTo(Map.of("message", "Account verified successfully"));
        verify(authenticationService, times(1)).verifyUser(verifyDto);
    }

    @Test
    void resendVerificationCode_shouldReturnOkMessage() throws Exception {
        // Arrange
        String email = "test@example.com";
        doNothing().when(authenticationService).resendVerificationCode(email);

        // Act
        ResponseEntity<?> result = controller.resendVerificationCode(email);

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(Map.of("message", "Verification code sent"));
        verify(authenticationService, times(1)).resendVerificationCode(email);
    }

    @Test
    void forgotPassword_shouldReturnOkMessage() throws Exception {
        // Arrange
        String email = "test@example.com";
        doNothing().when(authenticationService).generateResetToken(email);

        // Act
        ResponseEntity<?> result = controller.forgotPassword(email);

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(Map.of("message", "Email de réinitialisation envoyé"));
        verify(authenticationService, times(1)).generateResetToken(email);
    }

    @Test
    void resetPassword_shouldReturnOkMessage() throws Exception {
        // Arrange
        String token = "reset-token";
        String newPassword = "newPassword";
        Map<String, String> body = Map.of("token", token, "newPassword", newPassword);
        doNothing().when(authenticationService).resetPassword(token, newPassword);

        // Act
        ResponseEntity<?> result = controller.resetPassword(body);

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(Map.of("message", "Mot de passe réinitialisé avec succès"));
        verify(authenticationService, times(1)).resetPassword(token, newPassword);
    }

    @Test
    void refreshToken_shouldCallService() throws IOException {
        // Act
        controller.refreshToken(request, response);

        // Assert
        verify(authenticationService, times(1)).refreshToken(request, response);
    }
}
