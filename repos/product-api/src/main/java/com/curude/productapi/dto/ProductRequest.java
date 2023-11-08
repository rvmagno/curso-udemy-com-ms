package com.curude.productapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRequest {


    private String name;

    private Integer supplier;

    private Integer category;

    @JsonProperty("quantity_available")
    private Integer qtyAvailable;
}
