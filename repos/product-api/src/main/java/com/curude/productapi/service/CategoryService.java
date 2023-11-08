package com.curude.productapi.service;

import com.curude.productapi.config.exception.SuccessResponse;
import com.curude.productapi.config.exception.ValidationException;
import com.curude.productapi.dto.CategoryRequest;
import com.curude.productapi.dto.CategoryResponse;
import com.curude.productapi.model.Category;
import com.curude.productapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ProductService productService;

    public Category findById(Integer id){
        validateInformedId(id);
        return repository.findById(id).orElseThrow(() -> new ValidationException("There's no Category for the given ID."));
    }

    public List<CategoryResponse> findAll(){
        return repository.findAll()
            .stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

    public List<CategoryResponse> findByDescription(String description){
        if(isEmpty(description)){
            throw new ValidationException("the category description must be informed");
        }
        return repository.findByDescriptionIgnoreCaseContaining(description)
            .stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());

    }

    public CategoryResponse save(CategoryRequest request){
        validateCategoryNameInformed(request);
        var category = repository.save(Category.of(request));
        return CategoryResponse.of(category);
    }


    public CategoryResponse update(CategoryRequest request, Integer id){
        validateCategoryNameInformed(request);
        validateInformedId(id);
        var category = Category.of(request);
        category.setId(id);
        repository.save(category);
        return CategoryResponse.of(category);
    }


    public SuccessResponse delete(Integer id){
        validateInformedId(id);

        if(productService.existsByCattegoryId(id)){
            throw new ValidationException("You cannot delete this category because it's already defined by a product");
        }

        repository.deleteById(id);
        return SuccessResponse.create("the category was deleted");
    }


    private void validateInformedId(Integer id){
        if(isEmpty(id)){
            throw new ValidationException("the category ID must be informed");
        }
    }


    private void validateCategoryNameInformed(CategoryRequest request){
        if(isEmpty(request.getDescription())){
            throw new ValidationException("the category description wasn't informed.");
        }
    }
}
