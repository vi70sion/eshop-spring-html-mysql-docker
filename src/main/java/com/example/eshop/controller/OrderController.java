package com.example.eshop.controller;

import com.example.eshop.model.Order;
import com.example.eshop.service.OrderService;
import com.example.eshop.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RestController
public class OrderController {

    OrderService orderService = new OrderService();

    // Get all orders or filter by payment status (paid/unpaid)
    @CrossOrigin
    @GetMapping("/orders")
    public List<Order> getOrders(@RequestParam(value = "paymentStatus", required = false) String paymentStatus,
                                 @RequestHeader("Authorization") String authorizationHeader) {
        if(!TokenService.handleAuthorization(authorizationHeader).getBody().equals("authorized"))
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        if (paymentStatus == null) {
            // No payment status, return all orders
            return ResponseEntity.ok(orderService.getAllOrders()).getBody();
        } else if (paymentStatus.equalsIgnoreCase("paid")) {
            // Return only paid orders
            return orderService.getOrdersByPaymentStatus("paid");
        } else if (paymentStatus.equalsIgnoreCase("unpaid")) {
            // Return only unpaid orders
            return orderService.getOrdersByPaymentStatus("unpaid");
        } else {
            // Handle invalid status (optional)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment status");
        }
    }

    @CrossOrigin
    @GetMapping("/orders/amount/{id}")
    public ResponseEntity<BigDecimal> orderAmount(@PathVariable long id,@RequestHeader("Authorization") String authorizationHeader) {
        return TokenService.handleAuthorization(authorizationHeader).getBody().equals("authorized") ?
                ResponseEntity.ok(orderService.orderAmount(id)):
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BigDecimal.ZERO);
        //TODO reikia padaryti tikrinimą, jei orderAmount(id) grąžina 0.00, negrąžinti Http statuso Ok
    }

}
