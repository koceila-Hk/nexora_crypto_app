package com.nexora.nexora_crypto_api.service.impl;

import com.nexora.nexora_crypto_api.model.User;
import com.nexora.nexora_crypto_api.model.dto.LoginUserDto;
import com.nexora.nexora_crypto_api.model.dto.RegisterUserDto;
import com.nexora.nexora_crypto_api.model.dto.VerifyUserDto;
import com.nexora.nexora_crypto_api.repository.UserRepository;
import com.nexora.nexora_crypto_api.utils.EmailService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthentificationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock // sert à créer un faux objet
    private EmailService emailService;

    @InjectMocks // sert à créer une instance réelle de la classe à tester
    private AuthentificationServiceImpl authenticationService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void signup_shouldRegisterUserAndSendVerificationEmail() throws Exception {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail("test@example.com");
        dto.setPseudonym("testUser");
        dto.setPassword("Password123!");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByPseudonym(dto.getPseudonym())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        authenticationService.signup(dto);

        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(any(User.class));
    }

    @Test
    void signup_shouldThrowExceptionIfEmailExists() {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail("test@example.com");
        dto.setPseudonym("testUser");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> authenticationService.signup(dto));
        assertEquals("Utilisateur déjà existant", exception.getMessage());
    }

    @Test
    void authenticate_shouldReturnUserIfCorrectCredentials() throws Exception {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail("user@example.com");
        dto.setPassword("Password123!");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setEnabled(true);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        User result = authenticationService.authenticate(dto);

        assertEquals(user.getEmail(), result.getEmail());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void authenticate_shouldThrowExceptionIfUserNotFound() {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail("unknown@example.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> authenticationService.authenticate(dto));
        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    @Test
    void authenticate_shouldThrowExceptionIfAccountNotVerified() {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail("user@example.com");
        dto.setPassword("Password123!");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setEnabled(false);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(Exception.class, () -> authenticationService.authenticate(dto));
        assertEquals("Compte non vérifié", exception.getMessage());
    }

    @Test
    void verifyUser_shouldEnableAccountIfCodeIsCorrectAndNotExpired() throws Exception {
        String email = "test@example.com";
        String code = "123456";

        User user = new User();
        user.setEmail(email);
        user.setVerificationCode(code);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10)); // Pas expiré

        VerifyUserDto dto = new VerifyUserDto();
        dto.setEmail(email);
        dto.setVerificationCode(code);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        authenticationService.verifyUser(dto);

        assertTrue(user.isEnabled());
        assertNull(user.getVerificationCode());
        verify(userRepository).save(user);
    }

    @Test
    void verifyUser_shouldThrowExceptionIfCodeExpired() {
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1)); // Expiré

        VerifyUserDto dto = new VerifyUserDto();
        dto.setEmail(email);
        dto.setVerificationCode("123456");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(Exception.class, () -> authenticationService.verifyUser(dto));
        assertEquals("Code expiré", exception.getMessage());
    }

    @Test
    void verifyUser_shouldThrowExceptionIfCodeIncorrect() {
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10));

        VerifyUserDto dto = new VerifyUserDto();
        dto.setEmail(email);
        dto.setVerificationCode("654321");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(Exception.class, () -> authenticationService.verifyUser(dto));
        assertEquals("Code invalide", exception.getMessage());
    }

    @Test
    void resendVerificationCode_shouldGenerateAndSendNewCode() throws Exception {
        String email = "resend@example.com";
        User user = new User();
        user.setEmail(email);
        user.setEnabled(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        authenticationService.resendVerificationCode(email);

        assertNotNull(user.getVerificationCode());
        assertNotNull(user.getVerificationCodeExpiresAt());
        verify(userRepository).save(user);
        verify(emailService).sendVerificationEmail(user);
    }

    @Test
    void resendVerificationCode_shouldThrowIfAccountAlreadyVerified() {
        String email = "verified@example.com";
        User user = new User();
        user.setEmail(email);
        user.setEnabled(true); // déjà vérifié

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(Exception.class, () -> authenticationService.resendVerificationCode(email));
        assertEquals("Compte déjà vérifié", exception.getMessage());
    }

    @Test
    void resetPassword_shouldUpdatePasswordIfTokenValidAndNotExpired() throws Exception {
        String token = "resettoken";
        User user = new User();
        user.setResetToken(token);
        user.setTokenExpiryDate(Instant.now().plusSeconds(3600));

        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("NewPass123!")).thenReturn("hashedPassword");

        authenticationService.resetPassword(token, "NewPass123!");

        assertNull(user.getResetToken());
        assertNull(user.getTokenExpiryDate());
        verify(userRepository).save(user);
    }

    @Test
    void resetPassword_shouldThrowExceptionIfTokenExpired() {
        String token = "expiredtoken";
        User user = new User();
        user.setResetToken(token);
        user.setTokenExpiryDate(Instant.now().minusSeconds(1)); // expiré

        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(Exception.class, () -> authenticationService.resetPassword(token, "NewPassword!"));
        assertEquals("Token expiré", exception.getMessage());
    }

}
