package com.nexora.nexora_crypto_api.utils;

import static org.mockito.Mockito.*;

import com.nexora.nexora_crypto_api.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;


class EmailServiceTest {

    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService();

        // Injection manuelle du JavaMailSender mocké
        ReflectionTestUtils.setField(emailService, "emailSender", mailSender);
        ReflectionTestUtils.setField(emailService, "frontendUrl", "http://localhost:4200");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void sendEmail_shouldCallJavaMailSender() throws MessagingException {
        emailService.sendEmail("test@example.com", "Subject", "<p>Hello</p>");
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendVerificationEmail_shouldCallSendEmail() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setVerificationCode("123456");

        emailService.sendVerificationEmail(user);
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendResetPasswordEmail_shouldCallSendEmail() {
        String email = "user@example.com";
        String token = "token123";

        emailService.sendResetPasswordEmail(email, token);
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void resendResetPasswordEmail_shouldCallSendResetPasswordEmail() {
        EmailService spyService = spy(emailService);
        doNothing().when(spyService).sendResetPasswordEmail(anyString(), anyString());

        spyService.resendResetPasswordEmail("test@example.com", "token123");
        verify(spyService).sendResetPasswordEmail("test@example.com", "token123");
    }
}
