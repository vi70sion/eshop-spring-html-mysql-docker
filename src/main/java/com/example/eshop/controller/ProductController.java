package com.example.eshop.controller;

import com.example.eshop.JwtDecoder;
import com.example.eshop.model.Product;
import com.example.eshop.service.ProductService;
import com.example.eshop.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @CrossOrigin
    @PostMapping("/products/add")
    public ResponseEntity<String> addProduct(@RequestBody Product product,
                                             @RequestHeader("Authorization") String authorizationHeader ) {
        if(!UserService.isTokenCorrect(authorizationHeader))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("bad request");
        if(!UserService.authorize(authorizationHeader))
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("unauthorized");

        return  (productService.addProduct(product)) ?
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body("success"):
                ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("failed");
    }


}
