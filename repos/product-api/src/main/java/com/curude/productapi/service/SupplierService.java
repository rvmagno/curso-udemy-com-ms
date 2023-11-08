package com.curude.productapi.service;

import com.curude.productapi.config.exception.SuccessResponse;
import com.curude.productapi.config.exception.ValidationException;
import com.curude.productapi.dto.SupplierRequest;
import com.curude.productapi.dto.SupplierResponse;
import com.curude.productapi.model.Supplier;
import com.curude.productapi.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private ProductService productService;

    public SupplierResponse save(SupplierRequest req){
        return SupplierResponse.of(repository.save(Supplier.of(req)));
    }

    public SupplierResponse update(SupplierRequest req, Integer id){
        validateNameInformed(req.getName());
        validateInformedId(id);
        var supplier = Supplier.of(req);
        supplier.setId(id);

        return SupplierResponse.of(repository.save(supplier));
    }

    public Supplier findById(Integer id){
        validateInformedId(id);
        return repository.findById(id).orElseThrow(() -> new ValidationException("There's no Supplier for the given ID."));
    }

    public List<SupplierResponse> findByName(String name){
        validateNameInformed(name);
        return repository.findByNameIgnoreCaseContaining(name)
            .stream()
            .map(SupplierResponse::of)
            .collect(Collectors.toList());
    }

    public List<SupplierResponse> findAll(){
        return repository.findAll()
            .stream()
            .map(SupplierResponse::of)
            .collect(Collectors.toList());

    }


    public SuccessResponse delete(Integer id){
        validateInformedId(id);

        if(productService.existsBySupplierId(id)){
            throw new ValidationException("You cannot delete this suplier because it's already defined by a product");
        }

        repository.deleteById(id);
        return SuccessResponse.create("the supplier was deleted");
    }


    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("the supplier ID must be informed");
        }
    }

    private void validateNameInformed(String name){
        if(isEmpty(name)){
            throw new ValidationException("There's no Supplier for the given name.");
        }
    }

}
