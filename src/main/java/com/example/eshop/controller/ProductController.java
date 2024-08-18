package com.example.eshop.controller;

import com.example.eshop.model.Product;
import com.example.eshop.service.ProductService;
import com.example.eshop.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProductController {
    ProductService productService =new ProductService();

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProductsList() {
        return ResponseEntity.ok(productService.getAllProductsList());
    }

    @CrossOrigin
    @PostMapping("/products/add")
    public ResponseEntity<String> addProduct(@RequestBody Product product,
                                             @RequestHeader("Authorization") String authorizationHeader ) {
        if(!TokenService.handleAuthorization(authorizationHeader).getBody().equals("authorized"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
        return  (productService.addProduct(product)) ?
                ResponseEntity.ok("success"):
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("failed");
    }

    @CrossOrigin
    @PutMapping("/products/update")
    public ResponseEntity <String> updateProduct(@RequestBody Product product, @RequestHeader("Authorization") String authorizationHeader) {
        if(!TokenService.handleAuthorization(authorizationHeader).getBody().equals("authorized"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
        return (productService.updateProduct(product)) ?
                ResponseEntity.ok("success"):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failed");

    }

    @CrossOrigin
    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable long id, @RequestHeader("Authorization") String authorizationHeader) {
        if(!TokenService.handleAuthorization(authorizationHeader).getBody().equals("authorized"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
        return (productService.deleteProductById(id)) ?
                ResponseEntity.status(HttpStatus.OK).body("success") :
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("failed");
    }

    @CrossOrigin
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Product product = productService.getProductById(id);
        return (product == null) ?
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null):
                ResponseEntity.status(HttpStatus.OK).body(product);

    }


}
