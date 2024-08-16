package com.example.eshop.model;

public class OrderProduct {
    private long productId;
    private int quantity;

    public OrderProduct() {
    }
    public OrderProduct(long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    public long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public void setProductId(long productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

}
