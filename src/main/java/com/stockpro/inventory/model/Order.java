package com.stockpro.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private String customerName;
    private String customerEmail;
    private Long productId;
    private String productName;
    private Integer totalItems;
    private Double totalAmount;
    private String status = "Pending";
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String o) { this.orderId = o; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String n) { this.customerName = n; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String e) { this.customerEmail = e; }
    public Long getProductId() { return productId; }
    public void setProductId(Long p) { this.productId = p; }
    public String getProductName() { return productName; }
    public void setProductName(String p) { this.productName = p; }
    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer t) { this.totalItems = t; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double t) { this.totalAmount = t; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime t) { this.createdAt = t; }
}