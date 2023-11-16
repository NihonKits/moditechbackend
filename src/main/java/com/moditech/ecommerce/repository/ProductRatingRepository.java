package com.moditech.ecommerce.repository;

import com.moditech.ecommerce.model.ProductRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRatingRepository extends MongoRepository<ProductRating, String> {

    List<ProductRating> findByProductId(String productId);

    Optional<ProductRating> findByEmailAndProductId(String email, String productId);
}