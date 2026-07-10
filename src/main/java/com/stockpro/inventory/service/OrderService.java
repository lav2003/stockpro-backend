package com.stockpro.inventory.service;

import com.stockpro.inventory.model.Order;
import com.stockpro.inventory.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        order.setOrderId("#ORD-" + System.currentTimeMillis());
        order.setStatus("Pending");
        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> getByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}