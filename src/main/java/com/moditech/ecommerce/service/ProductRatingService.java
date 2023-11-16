package com.moditech.ecommerce.service;

import com.moditech.ecommerce.model.ProductRating;
import com.moditech.ecommerce.repository.ProductRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductRatingService {

    @Autowired
    ProductRatingRepository productRatingRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    //service
    public void addRating(ProductRating productRating) {
        Optional<ProductRating> existingRating = productRatingRepository.findByEmailAndProductId(productRating.getEmail(), productRating.getProductId());
        if (existingRating.isPresent()) {
            ProductRating rating = existingRating.get();
            rating.setRating(productRating.getRating());
            productRatingRepository.save(rating);
        } else {
            productRatingRepository.save(productRating);
        }
    }


    public Double calculateAverageRatingByEmailAndProductId(String email, String productId) {
        List<ProductRating> ratings = mongoTemplate.find(
                Query.query(Criteria.where("email").is(email).and("productId").is(productId)),
                ProductRating.class
        );

        if (!ratings.isEmpty()) {
            double sum = ratings.stream().mapToInt(ProductRating::getRating).sum();
            return sum / ratings.size();
        } else {
            return 0.0;
        }
    }

    public float getAverageRatingPercentage() {
        List<ProductRating> productRatings = productRatingRepository.findAll();
        float sumRatings = productRatings.stream().map(ProductRating::getRating).reduce(0, Integer::sum);
        float averageRating = sumRatings / productRatings.size();
        return averageRating * 100 / 5;
    }

}
