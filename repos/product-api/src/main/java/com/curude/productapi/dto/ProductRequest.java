package com.curude.productapi.dto;

import com.curude.productapi.model.Category;
import com.curude.productapi.model.Supplier;

public class ProductRequest {


    private String name;

    private Supplier supplier;

    private Category category;

    private Integer qtyAvailable;
}
