package com.stockpro.inventory.service;

import com.stockpro.inventory.model.Order;
import com.stockpro.inventory.model.Product;
import com.stockpro.inventory.repository.OrderRepository;
import com.stockpro.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                         ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getMyOrders(String customerEmail) {
        return orderRepository.findByCustomerEmailOrderByIdDesc(customerEmail);
    }

    @Transactional
    public Order createOrder(Order order) {
        if (order.getProductId() == null) {
            throw new RuntimeException("Product is required");
        }

        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int requestedQty = order.getTotalItems() == null ? 0 : order.getTotalItems();
        int available = product.getQuantity() == null ? 0 : product.getQuantity();

        if (requestedQty <= 0) {
            throw new RuntimeException("Invalid quantity");
        }
        if (requestedQty > available) {
            throw new RuntimeException("Only " + available + " item(s) left in stock");
        }

        // Deduct stock (Stock Lock)
        int remaining = available - requestedQty;
        product.setQuantity(remaining);
        if (remaining == 0) product.setStatus("Out of Stock");
        else if (remaining <= 5) product.setStatus("Low Stock");
        else product.setStatus("In Stock");
        productRepository.save(product);

        order.setOrderId("#ORD-" + System.currentTimeMillis());
        order.setProductName(product.getName());
        order.setStatus("Pending");
        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // If cancelling, restock the product
        if ("Cancelled".equalsIgnoreCase(status) && !"Cancelled".equalsIgnoreCase(order.getStatus())) {
            if (order.getProductId() != null) {
                productRepository.findById(order.getProductId()).ifPresent(product -> {
                    int restored = (product.getQuantity() == null ? 0 : product.getQuantity())
                            + (order.getTotalItems() == null ? 0 : order.getTotalItems());
                    product.setQuantity(restored);
                    if (restored == 0) product.setStatus("Out of Stock");
                    else if (restored <= 5) product.setStatus("Low Stock");
                    else product.setStatus("In Stock");
                    productRepository.save(product);
                });
            }
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> getByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

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