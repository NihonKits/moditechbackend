package com.moditech.ecommerce.controller;

import com.moditech.ecommerce.dto.ProductDto;
import com.moditech.ecommerce.model.Category;
import com.moditech.ecommerce.model.Product;
import com.moditech.ecommerce.repository.CategoryRepository;
import com.moditech.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductDto productDTO) {
//        Optional<Category> optionalCategories = categoryRepository.findById(productDTO.getCategoryId());
//        if (optionalCategories.isEmpty()) {
//            return ResponseEntity.ok("category does not exist");
//        } else {
            productService.createProduct(productDTO);
            return ResponseEntity.ok("success created product");
//        }
    }

    @GetMapping("/withCategory/{category}")
    List<Product> getProductWithSpecificCategory(@PathVariable Category category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/list")
    List<Product> getAllProducts(@RequestParam(name = "category", required = false) Category category) {
        if (category != null) {
            return productService.getProductsByCategory(category);
        } else {
            return productService.getAllProducts();
        }
    }

    @GetMapping("/bestProducts")
    List<Product> getBest4Products() {
        return productService.getTopSoldProducts();
    }

//    @GetMapping("/favoriteByEmail/{email}")
//    ResponseEntity<List<Optional<Product>>> getProductsByFavoriteFindByEmail(@PathVariable String email) {
//        List<Optional<Product>> productListFavoriteFindByEmail = productService.getProductsByFavoriteFindByEmail(email);
//        return ResponseEntity.ok(productListFavoriteFindByEmail);
//    }

    @GetMapping("/specificProduct/{id}")
    ResponseEntity<Optional<Product>> getProductById(@PathVariable String id) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        return ResponseEntity.ok(optionalProduct);
    }

    @DeleteMapping("/delete/{productId}")
    private String deleteProductById(@PathVariable("productId") String productId) {
        productService.deleteProductById(productId);
        return "product deleted";
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product product){
         Product products = productService.updateProduct(id, product);
        return ResponseEntity.ok(products);
    }

//    @PutMapping("/reason/{id}")
//    private ResponseEntity<Product> updatePutReason(@PathVariable String id, @RequestBody Product product){
//        Product products = productService.updatePutReason(id, product);
//        return ResponseEntity.ok(products);
//    }
}
