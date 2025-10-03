package com.matchmeeat.email.service;

import com.matchmeeat.email.EmailTemplates;
import com.matchmeeat.exception.customexceptions.EmailNotSentException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${ENV_SERVER_URL}")
    private String serverUrl;
    @Value("${custom.email.verify-token.ttl}")
    private Duration ttl;

    @Async
    public void sendPlainTextEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception exception) {
            log.error("Error while sending plain text e-mail", exception);
            throw new EmailNotSentException(exception.getMessage());
        }
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "UTF-8");

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (Exception exception) {
            log.error("Error while sending html e-mail", exception);
            throw new EmailNotSentException(exception.getMessage());
        }
    }

    public void sendVerificationEmail(String targetEmail, String username, String verificationToken) {
        String verificationEmailTitle = "Verify e-mail for your Match Me Eat account";
        String verificationEmailBody = createVerificationEmailBody(username, verificationToken);
        sendHtmlEmail(targetEmail, verificationEmailTitle, verificationEmailBody);
    }

    private String createVerificationEmailBody(String username, String verificationToken) {
        String template = EmailTemplates.verificationEmailBody();
        String verificationUrl = createVerificationUrl(verificationToken);
        return template.formatted(username, verificationUrl, ttl.toMinutes());
    }

    private String createVerificationUrl(String verificationToken) {
        String verifyEndpoint = "/api/auth/verify-email?token=" + verificationToken;
        return serverUrl + verifyEndpoint;
    }
}
