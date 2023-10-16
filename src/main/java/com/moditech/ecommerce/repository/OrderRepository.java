package com.moditech.ecommerce.repository;


import com.moditech.ecommerce.dto.OrderCountDto;
import com.moditech.ecommerce.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByEmail(String email);

    List<Order> findByStatus(String status);
}