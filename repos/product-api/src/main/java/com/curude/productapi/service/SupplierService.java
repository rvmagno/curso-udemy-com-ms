package com.curude.productapi.service;

import com.curude.productapi.dto.SupplierRequest;
import com.curude.productapi.dto.SupplierResponse;
import com.curude.productapi.model.Supplier;
import com.curude.productapi.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository repository;

    public SupplierResponse save (SupplierRequest req){
        return
            SupplierResponse
                .of(repository
                    .save(Supplier.of(req)));
    }

    public List<SupplierResponse> findAll(){
        var suppliers = new ArrayList<SupplierResponse>();
        repository.findAll().forEach(entity ->{
            suppliers.add(SupplierResponse.of(entity));
        });
        return suppliers;
    }

}
