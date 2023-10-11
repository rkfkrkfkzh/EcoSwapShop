package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("여기에 이메일@naver.com");   // <-- 여기에 당신의 네이버 이메일 주소를 추가하세요. application.yml에도 있다
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        javaMailSender.send(mailMessage);
    }
}
