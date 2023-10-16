package com.moditech.ecommerce.repository;

import com.moditech.ecommerce.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
