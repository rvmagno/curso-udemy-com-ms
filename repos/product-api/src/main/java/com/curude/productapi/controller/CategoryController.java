package com.curude.productapi.controller;

import com.curude.productapi.dto.CategoryRequest;
import com.curude.productapi.dto.CategoryResponse;
import com.curude.productapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> save(@RequestBody CategoryRequest request){

        return
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listCategory(){
        return
            ResponseEntity
                .status(HttpStatus.OK)
                .body(service.listCategories());
    }
}
