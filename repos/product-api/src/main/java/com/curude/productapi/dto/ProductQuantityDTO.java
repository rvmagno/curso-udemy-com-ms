package com.curude.productapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  @AllArgsConstructor @NoArgsConstructor
public class ProductQuantityDTO {

    private Integer productId;
    private Integer quantity;

}
