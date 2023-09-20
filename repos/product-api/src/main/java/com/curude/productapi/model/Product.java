package com.curude.productapi.model;

import com.curude.productapi.dto.ProductRequest;
import com.curude.productapi.dto.SupplierRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
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

    @Column(nullable = false)
    private Integer qtyAvailable;


    public static Product of(ProductRequest request){
        var entity = new Product();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }


}
