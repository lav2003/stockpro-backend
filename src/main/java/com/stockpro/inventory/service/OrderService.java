package com.stockpro.inventory.service;

import com.stockpro.inventory.model.Order;
import com.stockpro.inventory.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getMyOrders(String customerEmail) {
        return orderRepository.findByCustomerEmailOrderByIdDesc(customerEmail);
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

    // Admin-only revenue/summary snapshot
    public Map<String, Object> getSummary() {
        List<Order> all = orderRepository.findAll();
        long pending = all.stream().filter(o -> "Pending".equals(o.getStatus())).count();
        long completed = all.stream().filter(o -> "Completed".equals(o.getStatus())).count();
        long cancelled = all.stream().filter(o -> "Cancelled".equals(o.getStatus())).count();
        double revenue = all.stream()
                .filter(o -> "Completed".equals(o.getStatus()))
                .mapToDouble(o -> o.getTotalAmount() == null ? 0.0 : o.getTotalAmount())
                .sum();
        return Map.of(
                "totalOrders", all.size(),
                "pending", pending,
                "completed", completed,
                "cancelled", cancelled,
                "revenue", revenue
        );
    }
}