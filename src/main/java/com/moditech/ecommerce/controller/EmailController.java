package com.moditech.ecommerce.controller;

import com.moditech.ecommerce.dto.TopSoldProductDto;
import com.moditech.ecommerce.model.Product;
import com.moditech.ecommerce.service.EmailService;
import com.moditech.ecommerce.service.OrderService;
import com.moditech.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/email")
@CrossOrigin("*")
public class EmailController {

    @Autowired
    EmailService emailService;

    @Autowired
    OrderService orderService;

    @Value("${frontend.base.url}")
    String frontEndBaseUrl;

    @PostMapping("/sendEmail/{email}")
    private ResponseEntity<String> sendEmail(@PathVariable String email, HttpServletResponse response)
            throws IOException {
        emailService.sendEmail(email);
        response.sendRedirect(frontEndBaseUrl);
        return ResponseEntity.ok("Email sent");
    }

    @GetMapping("/sendCombinedEmail")
    public ResponseEntity<String> sendCombinedEmail() {

        try {
            emailService.sendCombinedEmail();
            return ResponseEntity.ok("Emails sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send emails");
        }
    }
}
