package com.curude.productapi.controller;

import com.curude.productapi.config.exception.SuccessResponse;
import com.curude.productapi.dto.SupplierRequest;
import com.curude.productapi.dto.SupplierResponse;
import com.curude.productapi.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("supplier")
public class SupplierController {

    @Autowired
    private SupplierService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SupplierResponse>> findAll(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findAll());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<SupplierResponse> findById(@PathVariable Integer id){
        return
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(SupplierResponse.of(service.findById(id)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "name/{name}")
    public ResponseEntity<List<SupplierResponse>> findByDescription(@PathVariable String name){
        return
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(service.findByName(name));
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupplierResponse> save(@RequestBody SupplierRequest request){

        return
                ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(service.save(request));
    }


    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Integer id) {
        return
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(service.delete(id));
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "{id}")
    public ResponseEntity<SupplierResponse> update(@RequestBody SupplierRequest request, @PathVariable Integer id){
        return
                ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(service.update(request, id));
    }


}
