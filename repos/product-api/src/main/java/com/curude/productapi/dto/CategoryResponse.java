package com.curude.productapi.dto;

import com.curude.productapi.model.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryResponse {

    private Integer id;
    private String description;

    public static CategoryResponse of(Category entity){
        var response = new CategoryResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
