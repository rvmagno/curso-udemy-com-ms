package com.curude.productapi.controller;

import com.curude.productapi.config.exception.SuccessResponse;
import com.curude.productapi.dto.*;
import com.curude.productapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController{

    @Autowired
    private ProductService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> findAll(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findAll());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ProductResponse.of(service.findById(id)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "name/{name}")
    public ResponseEntity<List<ProductResponse>> findByName(@PathVariable String name){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findByName(name));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,  path = "supplier/{supplierId}")
    public ResponseEntity<List<ProductResponse>> findBySupplierId(@PathVariable Integer supplierId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findBySupplierId(supplierId));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> findByCategoryId(@PathVariable Integer categoryId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findByCategoryId(categoryId));
    }

    @PostMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> save(@RequestBody ProductRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<ProductResponse> update(@RequestBody ProductRequest request, @PathVariable Integer id){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.update(request, id));
    }


    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Integer id){
        return
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(service.delete(id));
    }

    @PostMapping("check-stock")
    public ResponseEntity<SuccessResponse> checkProductStock(@RequestBody ProductCheckStockRequest request){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.checkProductStock(request));
    }

    @GetMapping("${id}/sales")
    public ResponseEntity<ProductSalesResponse> findProductSales(@PathVariable Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findProductSales(id));
    }


}
