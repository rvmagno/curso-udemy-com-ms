package com.curude.productapi.controller;

import com.curude.productapi.config.exception.SuccessResponse;
import com.curude.productapi.dto.CategoryRequest;
import com.curude.productapi.dto.CategoryResponse;
import com.curude.productapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CategoryResponse>> findAll(){
        return
            ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findAll());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Integer id){
        return
            ResponseEntity
                .status(HttpStatus.OK)
                .body(CategoryResponse.of(service.findById(id)));
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "description/{description}")
    public ResponseEntity<List<CategoryResponse>> findByDescription(@PathVariable String description){
        return
            ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findByDescription(description));
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Integer id){
        return
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(service.delete(id));
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<CategoryResponse> update(@RequestBody CategoryRequest request, @PathVariable Integer id){

        return
                ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(service.update(request, id));
    }

}
