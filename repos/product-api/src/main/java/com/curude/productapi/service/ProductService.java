package com.curude.productapi.service;

import com.curude.productapi.config.exception.SuccessResponse;
import com.curude.productapi.config.exception.ValidationException;
import com.curude.productapi.dto.*;
import com.curude.productapi.dto.enums.SalesStatus;
import com.curude.productapi.model.Product;
import com.curude.productapi.rabbitmq.SalesConfirmationSender;
import com.curude.productapi.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class ProductService  {

    private static final Integer ZERO = 0;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SalesConfirmationSender salesConfirmationSender;

    @Autowired
    private SalesClient salesClient;

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

    public void updateProductStock(ProductStockDTO productStockDTO){

        try {
            validateStockUpdateData(productStockDTO);
            updateStock(productStockDTO);

        }catch (Exception ex){
            log.error("Error while trying to update stock. message -> {}", ex.getMessage(), ex);
            var rejectedMessage = new SalesConfirmationDTO(productStockDTO.getSalesId(), SalesStatus.REJECTED);
            salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);
        }
    }
    @Transactional
    private void updateStock(ProductStockDTO productStockDTO){
        var productsForUpdate = new ArrayList<Product>();
        productStockDTO.getProducts().forEach(prd ->{
            var existingProduct = findById(prd.getProductId());
            validateQUantityINStock(prd, existingProduct);
            existingProduct.updateStock(prd.getQuantity());
            productsForUpdate.add(existingProduct);
        });
        if(isEmpty(productsForUpdate)){
            repository.saveAll(productsForUpdate);
            salesConfirmationSender.sendSalesConfirmationMessage(new SalesConfirmationDTO(productStockDTO.getSalesId(), SalesStatus.APPROVED));
        }
    }

    private static void validateQUantityINStock(ProductQuantityDTO prd, Product existingProduct) {
        if(prd.getQuantity() > existingProduct.getQtyAvailable()){
            throw new ValidationException(String.format("the product %s is out of stock", existingProduct.getId())) ;
        }
    }


    private void validateStockUpdateData(ProductStockDTO dto) {
        if (isEmpty(dto) || isEmpty(dto.getSalesId())) {
            throw new ValidationException("the product OR salesId cannot be null");
        }

        if (isEmpty(dto.getProducts())) {
            throw new ValidationException("the sales' product cannot be null");
        }

        dto.getProducts().stream().forEach(prd -> {
            if (isEmpty(prd.getQuantity()) || isEmpty(prd.getProductId())) {

                throw new ValidationException("productId and quantity must be informed");
            }
        });

    }

    public ProductSalesResponse findProductSales(Integer id){
        var product = findById(id);
        try {
            var sales = salesClient.findSalesByProductId(id)
                    .orElseThrow(() -> new ValidationException("the sales was not found by this product"));
            return ProductSalesResponse.of(product, sales.getSalesIds());
        } catch (Exception e) {
            throw new ValidationException("There was an error trying to get the product's sale");
        }
    }


    public SuccessResponse checkProductStock(ProductCheckStockRequest request){
        if(isEmpty(request) || isEmpty(request.getProducts())){
            throw new ValidationException("Product Stock request must be informed");
        }
        request
            .getProducts()
                .forEach( this::validateStock);
        return SuccessResponse.create("the stock is ok!");

    }

    private void validateStock(ProductQuantityDTO dto){
        if(isEmpty(dto) || isEmpty(dto.getProductId()) || isEmpty(dto.getQuantity())){
            throw new ValidationException("Product Id and Quantity must be informed");
        }
        var product = findById(dto.getProductId());
        if(dto.getQuantity() >  product.getQtyAvailable()){
            throw new ValidationException(String.format("The product %s is out of stock.", product.getId()));
        }
    }
}
