package com.curude.productapi.service;

import com.curude.productapi.dto.ProductResponse;
import com.curude.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService  {

    @Autowired
    private ProductRepository repository;

    public List<ProductResponse> findAll(){
        var responses = new ArrayList<ProductResponse>();
        repository.findAll().forEach(entity ->{
            responses.add(ProductResponse.of(entity));
        });

        return responses;
    }
}
