package com.moditech.ecommerce.service;

import com.moditech.ecommerce.dto.OrderCountDto;
import com.moditech.ecommerce.dto.OrderDto;
import com.moditech.ecommerce.dto.ProductQuantityDto;
import com.moditech.ecommerce.dto.ProductVariationsDto;
import com.moditech.ecommerce.model.Order;
import com.moditech.ecommerce.model.Product;
import com.moditech.ecommerce.model.ProductVariations;
import com.moditech.ecommerce.repository.OrderRepository;
import com.moditech.ecommerce.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }


    public void createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setStatus(orderDto.getStatus());
        order.setEmail(orderDto.getEmail());
        order.setUserFullName(orderDto.getUserFullName());
        order.setOrderList(orderDto.getOrderList());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setOrderDate(orderDto.getOrderDate());

        List<ProductQuantityDto> productQuantities = orderDto.getProducts();
        subtractProductsFromInventory(productQuantities);
        updateProductSold(productQuantities);

        orderRepository.save(order);
    }

    private void subtractProductsFromInventory(List<ProductQuantityDto> productQuantities) {
        System.out.println(productQuantities);
        for (ProductQuantityDto pq : productQuantities) {
            Optional<Product> productOptional = productRepository.findById(pq.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                pq.getVariationIndexes().forEach(variationIndex -> {
                    if (variationIndex >= 0 && variationIndex < product.getProductVariationsList().size()) {
                        ProductVariations variation = product.getProductVariationsList().get(variationIndex);
                        int newQuantity = variation.getQuantity() - pq.getQuantity();
                        if (newQuantity < 0) {
                            // Handle negative quantity if needed
                            log.error("Negative quantity for product variation: {}", variation.getVariationName());
                        } else {
                            variation.setQuantity(newQuantity);
                        }
                    } else {
                        log.error("Invalid variation index: {}", variationIndex);
                    }
                });

                productRepository.save(product);
            } else {
                log.error("Product not found: {}", pq.getProductId());
            }
        }
    }

    public void updateProductSold(List<ProductQuantityDto> productQuantities) {
        for (ProductQuantityDto pq : productQuantities) {
            Optional<Product> productOptional = productRepository.findById(pq.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                pq.getVariationIndexes().forEach(variationIndex -> {
                    if (variationIndex >= 0 && variationIndex < product.getProductVariationsList().size()) {
                        ProductVariations variation = product.getProductVariationsList().get(variationIndex);
                        variation.setSold(variation.getSold() + pq.getQuantity());
                    } else {
                        log.error("Invalid variation index: {}", variationIndex);
                    }
                });

                productRepository.save(product);
            } else {
                log.error("Product not found: {}", pq.getProductId());
            }
        }
    }

    public List<Order> getOrderByEmail(String email) {
        return orderRepository.findByEmail(email);
    }

    public void uploadReceipt(String orderId, Order getOrder) {
        Order setOrder = orderRepository.findById(orderId).orElse(null);
        if (setOrder != null) {
            setOrder.setReceipt(getOrder.getReceipt());
            orderRepository.save(setOrder);
        }
    }

    public void updateOrderStatus(String id, Order order) {
        Order setOrder = orderRepository.findById(id).orElse(null);
        if (setOrder != null) {
            setOrder.setStatus(order.getStatus());
            orderRepository.save(setOrder);
        }
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    public double getTotalPriceByStatus() {
//        String status = "Delivered";
//        List<Order> orders = orderRepository.findByStatus(status);
        List<Order> orders = orderRepository.findAll();
        return orders.stream().mapToDouble(Order::getTotalPrice).sum();
    }

    public List<Map<String, Object>> getTotalSalesPerMonth() {
//        List<Order> deliveredOrders = orderRepository.findByStatus("Delivered");
        List<Order> allOrders = orderRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM");
        return allOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().format(formatter),
                        Collectors.summingDouble(Order::getTotalPrice)
                ))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("orderDate", entry.getKey());
                    result.put("totalPrice", entry.getValue());
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<OrderCountDto> getTop5Customers() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("email").count().as("orderCount").first("email").as("email"),
                Aggregation.sort(Sort.by(Sort.Order.desc("orderCount"))),
                Aggregation.limit(5)
        );

        AggregationResults<OrderCountDto> result = mongoTemplate.aggregate(aggregation, "order", OrderCountDto.class);
        return result.getMappedResults();
    }
}