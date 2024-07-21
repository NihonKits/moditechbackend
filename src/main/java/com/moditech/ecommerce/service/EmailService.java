package com.moditech.ecommerce.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moditech.ecommerce.dto.OrderCountDto;
import com.moditech.ecommerce.dto.OrderDetailsDto;
import com.moditech.ecommerce.dto.ProductDto;
import com.moditech.ecommerce.model.Order;
import com.moditech.ecommerce.model.ProductVariations;
import com.moditech.ecommerce.repository.OrderRepository;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderRepository orderRepository;

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
    public void sendCombinedEmail() {
        List<OrderCountDto> top5Customers = orderService.getTop5Customers();
        List<String> userEmails = top5Customers.stream().map(OrderCountDto::getEmail).collect(Collectors.toList());

        String subject = "Your Recent Orders";
        ObjectMapper objectMapper = new ObjectMapper();

        for (String userEmail : userEmails) {
            try {
                // Fetch orders for each customer
                List<Order> customerOrders = orderRepository.findByEmail(userEmail);

                // Parse orders into ProductDto objects
                List<ProductDto> customerProducts = new ArrayList<>();
                for (Order order : customerOrders) {
                    List<OrderDetailsDto> orderDetails = objectMapper.readValue(order.getOrderList(),
                            new TypeReference<List<OrderDetailsDto>>() {
                            });
                    for (OrderDetailsDto detail : orderDetails) {
                        ProductDto productDto = detail.getProduct();
                        ProductVariations selectedVariation = productDto.getProductVariationsList()
                                .get(detail.getVariationIndex());
                        productDto.setProductVariationsList(Collections.singletonList(selectedVariation));
                        customerProducts.add(productDto);
                    }
                }

                // Generate HTML content with customer products
                String htmlContent = generateOrderEmailHtml(customerProducts);

                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                helper.setTo(userEmail);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);
                helper.setFrom(adminEmail);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateOrderEmailHtml(List<ProductDto> customerProducts) {
        Context context = new Context();
        context.setVariable("customerProducts", customerProducts); // Add customer products to the context
        return templateEngine.process("email-template", context);
    }
}
