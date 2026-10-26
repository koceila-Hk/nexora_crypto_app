package com.nexora.nexora_crypto_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

// Configuration de l’envoi d’emails avec Spring Boot
@Configuration
public class EmailConfiguration {

    // Username/password optionnels : vides en local (Mailpit, sans authentification)
    @Value("${spring.mail.username:}")
    private String emailUsername;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.password:}")
    private String emailPassword;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
    private boolean startTls;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        if (!emailUsername.isBlank()) {
            mailSender.setUsername(emailUsername);
            mailSender.setPassword(emailPassword);
        }

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", String.valueOf(smtpAuth));
        props.put("mail.smtp.starttls.enable", String.valueOf(startTls));

        return mailSender;
    }
}