package com.nexora.nexora_crypto_api.utils;

import com.nexora.nexora_crypto_api.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private JavaMailSender emailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(message);
    }

    public void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            sendEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            logger.error("Error lors de l'envoie du mail de vérification à {}", user.getEmail());
        }
    }

    public void sendResetPasswordEmail(String to, String token)  {


        String resetLink = frontendUrl + "/#/reset-password?token=" + token;
        String subject = "Réinitialisation de votre mot de passe";
        String htmlMessage = "<html><body>"
                + "<p>Bonjour,</p>"
                + "<p>Pour réinitialiser votre mot de passe, cliquez sur le lien ci-dessous :</p>"
                + "<a href=\"" + resetLink + "\">Réinitialiser mon mot de passe</a>"
                + "<p>Ce lien est valable 1 heure.</p>"
                + "</body></html>";

        try {
            sendEmail(to, subject, htmlMessage);
        } catch (MessagingException e) {
            logger.error("Error lors de l'envoie du mail de rénitialisation à {}",to, e);
        }
    }

    public void resendResetPasswordEmail(String to, String token) {
        logger.info("Renvoie du mail de reset password à {}",to);
        sendResetPasswordEmail(to, token);
    }

}