package com.moditech.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String adminEmail;

    @Async
    public void sendEmail(String userEmail) {
        String subject = "Account Verification";
        String message = "Please click the following link to verify your account: http://localhost:8081/api/user/isEnable/userID/" + userEmail;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(adminEmail);
        javaMailSender.send(mailMessage);
    }
}
