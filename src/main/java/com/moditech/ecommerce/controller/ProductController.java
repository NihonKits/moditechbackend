package com.moditech.ecommerce.controller;

import com.moditech.ecommerce.dto.ProductDto;
import com.moditech.ecommerce.dto.ProductVariationsDto;
import com.moditech.ecommerce.dto.TopSoldProductDto;
import com.moditech.ecommerce.model.Product;
import com.moditech.ecommerce.model.ProductVariations;
import com.moditech.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/create")
    private ResponseEntity<Product> createProduct(@RequestBody ProductDto productDTO) {
        Product product = productService.createProduct(productDTO);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/list")
    private List<TopSoldProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/bestProducts")
    private List<TopSoldProductDto> getBestProducts() {
        return productService.getTopSoldProducts();
    }

    @GetMapping("/getProductsWithZeroSold")
    private List<TopSoldProductDto> getProductsWithZeroSold() {
        return productService.getProductsWithZeroSold();
    }

    @GetMapping("/specificProduct/{id}")
    private ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete/{productId}")
    private String deleteProductById(@PathVariable("productId") String productId) {
        productService.deleteProductById(productId);
        return "product deleted";
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        Product products = productService.updateProduct(id, product);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/product-variation/{id}")
    private ResponseEntity<Product> addProductVariation(@PathVariable String id, @RequestBody Product product) {
        Product products = productService.addProductVariation(id, product);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/isAd")
    private ResponseEntity<List<TopSoldProductDto>> getProductsByIsAd() {
        List<TopSoldProductDto> product = productService.getProductsByIsAd();
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}/variations/{variationName}")
    public ResponseEntity<String> removeProductVariation(
            @PathVariable String productId,
            @PathVariable String variationName) {
        try {
            productService.removeProductVariation(productId, variationName);
            return ResponseEntity.ok("Product variation removed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{productId}/variations/{oldVariationName}")
    public ResponseEntity<String> updateProductVariation(
            @PathVariable String productId,
            @PathVariable String oldVariationName,
            @RequestBody ProductVariationsDto updatedVariation) {
        try {
            productService.updateProductVariation(productId, oldVariationName, updatedVariation);
            return ResponseEntity.ok("Product variation updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{productId}/variations/{variationName}")
    public ResponseEntity<ProductVariations> getProductVariationByName(
            @PathVariable String productId,
            @PathVariable String variationName) {
        try {
            ProductVariations variation = productService.getProductVariationByName(productId, variationName);
            return ResponseEntity.ok(variation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
