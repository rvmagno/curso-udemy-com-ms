package com.curude.productapi.dto;

import com.curude.productapi.model.Category;
import com.curude.productapi.model.Supplier;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SupplierResponse {

    private Integer id;

    private String name;


    public static SupplierResponse of(Supplier entity){
        var response = new SupplierResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
