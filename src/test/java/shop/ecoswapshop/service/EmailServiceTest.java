package shop.ecoswapshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendEmail() {
        // Given
        String toEmail = "test@example.com";
        String subject = "Test Subject";
        String content = "Test content";

        // When
        emailService.sendEmail(toEmail, subject, content);

        // Then
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}