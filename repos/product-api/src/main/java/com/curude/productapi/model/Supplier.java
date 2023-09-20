package com.curude.productapi.model;

import com.curude.productapi.dto.SupplierRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name="SUPPLIER")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column (nullable = false)
    private String name;

    public static Supplier of(SupplierRequest request){
        var entity = new Supplier();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

}
