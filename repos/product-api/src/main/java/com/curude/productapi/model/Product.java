package com.curude.productapi.model;

import com.curude.productapi.dto.ProductRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column (nullable = false)
    private String name;

    @ManyToOne @JoinColumn(name = "FK_SUPPLIER", nullable = false)
    private Supplier supplier;

    @ManyToOne @JoinColumn(name = "FK_CATEGORY", nullable = false)
    private Category category;

    @Column(name="QTY_AVAILABLE", nullable = false)
    private Integer qtyAvailable;

    @Column(name="CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @PrePersist
    public void prePersist(){
        createAt = LocalDateTime.now();
    }

    public static Product of(ProductRequest request, Category category, Supplier supplier){


        return Product.builder()
            .name(request.getName())
            .category(category)
            .qtyAvailable(request.getQtyAvailable())
            .supplier(supplier)

        .build();
    }

    public void updateStock(Integer quantity){
        this.qtyAvailable = qtyAvailable - quantity;
    }

}
