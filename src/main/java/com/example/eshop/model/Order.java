package com.example.eshop.model;

import java.math.BigDecimal;

public class Order {
    private long id;
    private String products;
    private BigDecimal totalPrice;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private boolean paymentStatus;

    public Order() {
    }

    public Order(long id, String products, BigDecimal totalPrice, String customerName,
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
    public String getProducts() { return products; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getCustomerName() { return customerName; }
    public String getCustomerAddress() { return customerAddress; }
    public String getCustomerEmail() { return customerEmail; }
    public boolean isPaymentStatus() { return paymentStatus; }




}
