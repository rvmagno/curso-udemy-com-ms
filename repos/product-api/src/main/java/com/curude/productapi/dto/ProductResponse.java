package com.curude.productapi.dto;

import com.curude.productapi.model.Category;
import com.curude.productapi.model.Product;
import com.curude.productapi.model.Supplier;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ProductResponse {

    private Integer id;

    private String name;

    private Supplier supplier;

    private Category category;

    private Integer qtyAvailable;

    public static ProductResponse of(Product entity){
        var response = new ProductResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
