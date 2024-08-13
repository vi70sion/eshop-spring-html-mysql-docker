package com.example.eshop.service;

import com.example.eshop.model.Product;
import com.example.eshop.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


public class ProductService {

    ProductRepository prRepository = new ProductRepository();

    public List<Product> getAllProductsList() {
        return prRepository.getAllProductsList();
    }

    public boolean addProduct(Product product) {
        return prRepository.addProduct(product);
    }




}
