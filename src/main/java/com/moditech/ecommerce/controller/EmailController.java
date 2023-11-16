package com.moditech.ecommerce.controller;

import com.moditech.ecommerce.dto.OrderCountDto;
import com.moditech.ecommerce.dto.TopSoldProductDto;
import com.moditech.ecommerce.model.Product;
import com.moditech.ecommerce.service.EmailService;
import com.moditech.ecommerce.service.OrderService;
import com.moditech.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/email")
@CrossOrigin("*")
public class EmailController {

    @Autowired
    EmailService emailService;

    @Autowired
    private ProductService productService;

    @Autowired
    OrderService orderService;

    @Value("${frontend.base.url}")
    String frontEndBaseUrl;

    @PostMapping("/sendEmail/{email}")
    private ResponseEntity<String> sendEmail(@PathVariable String email, HttpServletResponse response) throws IOException {
        emailService.sendEmail(email);
        response.sendRedirect(frontEndBaseUrl);
        return ResponseEntity.ok("Email sent");
    }

    @PostMapping("/sendCombinedEmail")
    private ResponseEntity<String> sendCombinedEmail(@RequestBody List<String> emails, HttpServletResponse response) throws IOException {
        // Use the provided list of emails
        List<OrderCountDto> top5Customers = orderService.getTop5Customers();

        // Extract emails from top 5 customers
        List<String> customerEmails = top5Customers.stream().map(OrderCountDto::getEmail).collect(Collectors.toList());

        List<TopSoldProductDto> topSoldProducts = productService.getTopSoldProducts();
        List<Product> productsWithinLastMonth = productService.getProductsWithinLastMonth();

        // Send combined email to the specified emails
        for (String customerEmail : customerEmails) {
            if (emails.contains(customerEmail)) {
                emailService.sendCombinedEmail(Collections.singletonList(customerEmail), topSoldProducts, productsWithinLastMonth);
            }
        }

        response.sendRedirect(frontEndBaseUrl);
        return ResponseEntity.ok("Emails sent to selected customers");
    }
}
