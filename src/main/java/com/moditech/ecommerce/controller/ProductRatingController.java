package com.moditech.ecommerce.controller;

import com.moditech.ecommerce.model.ProductRating;
import com.moditech.ecommerce.repository.ProductRepository;
import com.moditech.ecommerce.service.ProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productRating")
public class ProductRatingController {

    @Autowired
    ProductRatingService productRatingService;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/rate")
    public ResponseEntity<?> rateProduct(@RequestBody ProductRating productRating) {
        productRatingService.addRating(productRating);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}/{productId}")
    public Double calculateAverageRating(@PathVariable String email, @PathVariable String productId) {
        return productRatingService.calculateAverageRatingByEmailAndProductId(email, productId);
    }

    @GetMapping("/customerRating")
    public ResponseEntity<Float> getAverageRatingPercentage() {
        float averageRatingPercentage = productRatingService.getAverageRatingPercentage();
        return ResponseEntity.ok(averageRatingPercentage);
    }
}
