package com.moditech.ecommerce.controller;

import com.moditech.ecommerce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/email")
@CrossOrigin("*")
public class EmailController {

    @Autowired
    EmailService emailService;

    @Value("${frontend.base.url}")
    String frontEndBaseUrl;

    @PostMapping("/sendEmail/{email}")
    private ResponseEntity<String> sendEmail(@PathVariable String email, HttpServletResponse response) throws IOException {
        emailService.sendEmail(email);
        response.sendRedirect(frontEndBaseUrl);
        return ResponseEntity.ok("Email sent");
    }
}
