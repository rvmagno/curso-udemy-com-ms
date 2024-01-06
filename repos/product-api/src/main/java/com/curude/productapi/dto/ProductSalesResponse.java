package com.curude.productapi.dto;

import com.curude.productapi.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data  @NoArgsConstructor  @AllArgsConstructor  @Builder
public class ProductSalesResponse {
    private Integer id;

    private String name;

    private SupplierResponse supplier;

    private CategoryResponse category;

    @JsonProperty("quantity_available")
    private Integer qtyAvailable;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createAt;

    private List<String> sales;


    public static ProductSalesResponse of(Product entity, List<String> sales){
        return  ProductSalesResponse
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .qtyAvailable(entity.getQtyAvailable())
                .supplier(SupplierResponse.of(entity.getSupplier()))
                .category(CategoryResponse.of(entity.getCategory()))
                .createAt(entity.getCreateAt() )
                .sales(sales)
                .build();
    }

}
