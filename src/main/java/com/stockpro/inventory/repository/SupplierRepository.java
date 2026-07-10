package com.stockpro.inventory.repository;

import com.stockpro.inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByStatus(String status);
}
