package com.example.eshop.controller;

import com.example.eshop.model.Product;
import com.example.eshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ProductController {
    ProductService productService =new ProductService();

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProductsList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getAllProductsList());
    }


}
