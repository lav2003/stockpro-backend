package com.stockpro.inventory.repository;

import com.stockpro.inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByCustomerNameContainingIgnoreCase(String name);
    List<Order> findByCustomerEmailOrderByIdDesc(String customerEmail);
}