package com.curude.productapi.repository;

import com.curude.productapi.model.Product;
import com.curude.productapi.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    List<Supplier> findByNameIgnoreCaseContaining(String name);
}
