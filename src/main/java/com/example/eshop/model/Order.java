package com.example.eshop.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class Order {
    private long id;
    private List<OrderProduct> products;
    private BigDecimal totalPrice;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private boolean paymentStatus;

    public Order() {
    }

    public Order(long id, List<OrderProduct> products, BigDecimal totalPrice, String customerName,
                 String customerAddress, String customerEmail, boolean paymentStatus) {
        this.id = id;
        this.products = products;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.paymentStatus = paymentStatus;
    }

    public long getId() { return id; }
    public List<OrderProduct> getProducts() { return products; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getCustomerName() { return customerName; }
    public String getCustomerAddress() { return customerAddress; }
    public String getCustomerEmail() { return customerEmail; }
    public boolean isPaymentStatus() { return paymentStatus; }

    public void setId(long id) { this.id = id; }
    public void setProducts(List<OrderProduct> products) { this.products = products; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setPaymentStatus(boolean paymentStatus) { this.paymentStatus = paymentStatus; }

}
