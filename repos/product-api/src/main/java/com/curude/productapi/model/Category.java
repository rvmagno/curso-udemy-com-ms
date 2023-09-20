package com.curude.productapi.model;

import com.curude.productapi.dto.CategoryRequest;
import com.curude.productapi.dto.CategoryResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="CATEGORY")
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String description;

    public static Category of(CategoryRequest request){
        var entity = new Category();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }
}
