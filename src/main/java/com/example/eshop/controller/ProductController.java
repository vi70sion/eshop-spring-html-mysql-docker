package com.example.eshop.controller;

import com.example.eshop.JwtDecoder;
import com.example.eshop.model.Product;
import com.example.eshop.service.ProductService;
import com.example.eshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    ProductService productService =new ProductService();

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProductsList(@RequestHeader("Authorization") String authorizationHeader) {
        return UserService.handleAuthorization(authorizationHeader).getBody().equals("authorized") ?
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(productService.getAllProductsList()):
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ArrayList<>());
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

    @CrossOrigin
    @PutMapping("/products/update")
    public ResponseEntity <String> updateProduct(@RequestBody Product product, @RequestHeader("Authorization") String authorizationHeader) {
        if(!UserService.isTokenCorrect(authorizationHeader)) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("bad request");
        if(!UserService.authorize(authorizationHeader)) return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("unauthorized");
        return (!productService.updateProduct(product)) ?
                new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>("success", HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable long id, @RequestHeader("Authorization") String authorizationHeader) {
        if(!UserService.isTokenCorrect(authorizationHeader)) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("bad request");
        if(!UserService.authorize(authorizationHeader)) return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("unauthorized");
        return (productService.deleteProductById(id)) ?
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body("success") :
                ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE)
                        .body("failed");
    }


}
