package com.moditech.ecommerce.service;

import com.moditech.ecommerce.dto.ProductDto;
import com.moditech.ecommerce.dto.TopSoldProductDto;
import com.moditech.ecommerce.model.Product;
import com.moditech.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setBarcode(productDto.getBarcode());
        product.setProductName(productDto.getProductName());
        product.setProductImage(productDto.getProductImage());
        product.setDescription(productDto.getDescription());
        product.setProductVariationsList(productDto.getProductVariationsList());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<TopSoldProductDto> getTopSoldProducts() {
        // Create a custom query to sum the sold quantities for each product
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("productVariationsList"), // Unwind the variations array
                Aggregation.group("_id")
                        .first("id").as("id")
                        .first("barcode").as("barcode")
                        .first("productName").as("productName")
                        .first("productImage").as("productImage")
                        .first("description").as("description")
                        .first("isAd").as("isAd")
                        .addToSet("productVariationsList").as("productVariationsList")
                        .sum("productVariationsList.sold").as("totalSold"), // Sum the sold quantities
                Aggregation.match(where("totalSold").gt(0)), // Exclude documents with totalSold of 0
                Aggregation.sort(Sort.Direction.DESC, "totalSold"), // Sort by total sold in descending order
                Aggregation.limit(8) // Limit the result to the top 8
        );

        // Execute the aggregation query
        AggregationResults<TopSoldProductDto> results = mongoTemplate.aggregate(aggregation, "product", TopSoldProductDto.class);
        // Get the result list
        return results.getMappedResults();
    }

    public List<TopSoldProductDto> getProductsWithZeroSold() {
        // Create a custom query to sum the sold quantities for each product
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("productVariationsList"), // Unwind the variations array
                Aggregation.group("_id")
                        .first("id").as("id")
                        .first("barcode").as("barcode")
                        .first("productName").as("productName")
                        .first("productImage").as("productImage")
                        .first("description").as("description")
                        .first("isAd").as("isAd")
                        .addToSet("productVariationsList").as("productVariationsList")
                        .sum("productVariationsList.sold").as("totalSold"), // Sum the sold quantities
                Aggregation.match(where("totalSold").is(0)), // Include only documents with totalSold of 0
                Aggregation.sort(Sort.Direction.DESC, "totalSold") // Sort by total sold in descending order
        );

        // Execute the aggregation query
        AggregationResults<TopSoldProductDto> results = mongoTemplate.aggregate(aggregation, "product", TopSoldProductDto.class);

        return results.getMappedResults();
    }



    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProductById(String productId) {
        productRepository.deleteById(productId);
    }

    public Product updateProduct(String id, Product product) {
        Product setProduct = productRepository.findById(id).orElse(null);

        assert setProduct != null;

        if (product.getProductName() != null && !product.getProductName().isEmpty()) {
            setProduct.setProductName(product.getProductName());
        }

        if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
            setProduct.setProductImage(product.getProductImage());
        }

        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            setProduct.setDescription(product.getDescription());
        }

        if (product.getBarcode() != null && !product.getBarcode().isEmpty()) {
            setProduct.setBarcode(product.getBarcode());
        }

        if (product.getIsAd() != null && !product.getIsAd().isEmpty()) {
            setProduct.setIsAd(product.getIsAd());
        }

        productRepository.save(setProduct);

        return setProduct;
    }

    public Product addProductVariation(String id, Product addProductVariation) {
        Product product = productRepository.findById(id).orElse(null);
        assert product != null;
        product.setProductVariationsList(addProductVariation.getProductVariationsList());
        return productRepository.save(product);
    }

    public List<Product> getProductsByIsAd() {
        String isAd = "true";

        return productRepository.findByIsAd(isAd);
    }


    private long monthlyTimestamp = 30;

    public List<Product> getProductsWithinLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(monthlyTimestamp, ChronoUnit.DAYS);
        return productRepository.findByCreatedAtAfter(oneMonthAgo);
    }
}
