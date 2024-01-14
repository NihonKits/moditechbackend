package com.moditech.ecommerce.service;

import com.moditech.ecommerce.dto.TopSoldProductDto;
import com.moditech.ecommerce.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    String adminEmail;

    @Value("${frontend.base.url}")
    String frontEndBaseUrl;

    @Async
    public void sendEmail(String userEmail) {
        String subject = "Account Verification";
        String message = "Please click the following link to verify your account:" + frontEndBaseUrl
                + "/api/user/isEnable/userID/" + userEmail;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(adminEmail);
        javaMailSender.send(mailMessage);
    }

    @Async
    public void sendCombinedEmail(List<String> userEmails, List<TopSoldProductDto> topSoldProducts,
            List<Product> productsWithinLastMonth) {
        String subject = "Combined Products";
        String htmlContent = generateCombinedProductsHtml(topSoldProducts, productsWithinLastMonth);

        for (String userEmail : userEmails) {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                helper.setTo(userEmail);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);
                helper.setFrom(adminEmail);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateCombinedProductsHtml(List<TopSoldProductDto> topSoldProducts,
            List<Product> productsWithinLastMonth) {
        Context context = new Context();
        context.setVariable("topSoldProducts", topSoldProducts);
        context.setVariable("productsWithinLastMonth", productsWithinLastMonth);
        return templateEngine.process("email-template", context);
    }
}
