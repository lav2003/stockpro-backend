package com.stockpro.inventory.service;

import com.stockpro.inventory.model.Product;
import com.stockpro.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        if (product.getQuantity() == 0) product.setStatus("Out of Stock");
        else if (product.getQuantity() <= 5) product.setStatus("Low Stock");
        else product.setStatus("In Stock");
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updated) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(updated.getName());
        product.setCategory(updated.getCategory());
        product.setPrice(updated.getPrice());
        product.setQuantity(updated.getQuantity());
        product.setSku(updated.getSku());
        if (product.getQuantity() == 0) product.setStatus("Out of Stock");
        else if (product.getQuantity() <= 5) product.setStatus("Low Stock");
        else product.setStatus("In Stock");
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}