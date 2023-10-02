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

    @Transactional
    public void sendTemporaryPassword(String toEmail, String tempPassword) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("임시 비밀번호 발급 안내");
        mailMessage.setText("귀하의 임시 비밀번호는 " + tempPassword + " 입니다.");

        javaMailSender.send(mailMessage);
    }
}
