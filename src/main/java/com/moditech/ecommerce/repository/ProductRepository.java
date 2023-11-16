package com.moditech.ecommerce.repository;

import com.moditech.ecommerce.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByIsAd(String isAd);

    List<Product> findByCreatedAtAfter(LocalDateTime timestamp);
}
