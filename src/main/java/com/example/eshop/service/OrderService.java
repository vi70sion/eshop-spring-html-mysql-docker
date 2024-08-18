package com.example.eshop.service;

import com.example.eshop.model.Order;
import com.example.eshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
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

    public BigDecimal addOrder(Order order, UUID uuid){
        return ordRepository.addOrder(order, uuid);
    }

    public boolean setOrderPaymentStatusTrue(UUID uuid){
        return ordRepository.setOrderPaymentStatusTrue(uuid);
    }

}
