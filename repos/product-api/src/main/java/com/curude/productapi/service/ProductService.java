package com.curude.productapi.service;

import com.curude.productapi.config.exception.SuccessResponse;
import com.curude.productapi.config.exception.ValidationException;
import com.curude.productapi.dto.ProductRequest;
import com.curude.productapi.dto.ProductResponse;
import com.curude.productapi.model.Product;
import com.curude.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService  {

    private static final Integer ZERO = 0;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryService categoryService;

    public List<ProductResponse> findAll(){
        return repository.findAll()
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public Product findById(Integer id){
        return repository.findById(id).orElseThrow(() -> new ValidationException("There's no Product for the given ID."));
    }

    public List<ProductResponse> findByName(String name){
        if(isEmpty(name)){
            throw new ValidationException("the product name was not informed");
        }
        return repository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supllierId){
        if(isEmpty(supllierId)){
            throw new ValidationException("the product name was not informed");
        }
        return repository.findBySupplierId(supllierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId){
        if(isEmpty(categoryId)){
            throw new ValidationException("the product name was not informed");
        }
        return repository.findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse save(ProductRequest request){
        validateProductDataInformed(request);
        validateCategoryAndSupplierNameInformed(request);
        var category = categoryService.findById(request.getCategory());
        var supplier = supplierService.findById(request.getSupplier());

        var product = repository.save(Product.of(request, category, supplier));
        return ProductResponse.of(product);
    }
    public ProductResponse update(ProductRequest request, Integer id){
        validateProductDataInformed(request);
        validateCategoryAndSupplierNameInformed(request);
        validateInformedId(id);

        var category = categoryService.findById(request.getCategory());
        var supplier = supplierService.findById(request.getSupplier());
        var product = Product.of(request, category, supplier);
        product.setId(id);

        repository.save(product);
        return ProductResponse.of(product);
    }


    public Boolean existsByCattegoryId(Integer id){
        return repository.existsByCategoryId(id);
    }

    public Boolean existsBySupplierId(Integer id){
        return repository.existsBySupplierId(id);
    }



    public SuccessResponse delete(Integer id){
        validateInformedId(id);

        repository.deleteById(id);
        return SuccessResponse.create("the product was deleted");
    }



    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("the category ID must be informed");
        }
    }


    private void validateProductDataInformed(ProductRequest request){
        if(isEmpty(request.getName())){
            throw new ValidationException("the product name was not informed");
        }

        if(isEmpty(request.getQtyAvailable())){
            throw new ValidationException("the quantity was not informed");
        }

        if(request.getQtyAvailable() <= ZERO){
            throw new ValidationException("the quantity should be greater then zero");
        }

    }
    private void validateCategoryAndSupplierNameInformed(ProductRequest request){
        if(isEmpty(request.getCategory())){
            throw new ValidationException("the category was not informed");
        }
        if(isEmpty(request.getSupplier())){
            throw new ValidationException("the supplier was not informed");
        }

    }
}
