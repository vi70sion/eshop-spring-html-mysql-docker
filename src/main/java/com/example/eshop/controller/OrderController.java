package com.example.eshop.controller;


import com.example.eshop.model.Order;
import com.example.eshop.service.OrderService;
import com.example.eshop.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class OrderController {

    OrderService orderService = new OrderService();

    @CrossOrigin
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrdersList(@RequestHeader("Authorization") String authorizationHeader) {
        return TokenService.handleAuthorization(authorizationHeader).getBody().equals("authorized") ?
                ResponseEntity.ok(orderService.getAllOrdersList()):
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
    }



}
