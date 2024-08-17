package com.example.eshop.service;

import com.example.eshop.model.Order;
import com.example.eshop.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {

    OrderRepository ordRepository = new OrderRepository();
    public OrderService() { }

    public List<Order> getAllOrders() {
        return ordRepository.findAllOrders();
    }

    public List<Order> getOrdersByPaymentStatus(String paymentStatus) {
        return ordRepository.findOrdersByPaymentStatus(paymentStatus);
    }

    public BigDecimal orderAmount(long id) {
        return ordRepository.orderAmount(id);
    }

}
