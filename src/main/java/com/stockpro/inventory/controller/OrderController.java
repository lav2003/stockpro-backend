package com.stockpro.inventory.controller;

import com.stockpro.inventory.model.Order;
import com.stockpro.inventory.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(@RequestParam String email) {
        return ResponseEntity.ok(orderService.getMyOrders(email));
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        return ResponseEntity.ok(orderService.getSummary());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Order order) {
        try {
            return ResponseEntity.ok(orderService.createOrder(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Admin: change status to any value
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    // Customer: cancel their own pending order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelMyOrder(@PathVariable Long id,
            @RequestParam String email) {
        try {
            return ResponseEntity.ok(orderService.cancelMyOrder(id, email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<Order>> getByStatus(@RequestParam String status) {
        return ResponseEntity.ok(orderService.getByStatus(status));
    }
}