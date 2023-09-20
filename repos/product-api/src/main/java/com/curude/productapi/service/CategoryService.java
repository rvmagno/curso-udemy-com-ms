package com.curude.productapi.service;

import com.curude.productapi.config.exception.ValidationException;
import com.curude.productapi.dto.CategoryRequest;
import com.curude.productapi.dto.CategoryResponse;
import com.curude.productapi.model.Category;
import com.curude.productapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<CategoryResponse> listCategories(){
        var responses = new ArrayList<CategoryResponse>();
        repository.findAll().forEach(entity ->{
            responses.add(CategoryResponse.of(entity));
        });
        return responses;
    }

    public CategoryResponse save(CategoryRequest request){
        validateCategoryNameInformed(request);
        var category = repository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    private void validateCategoryNameInformed(CategoryRequest request){
        if(isEmpty(request.getDescription())){
            throw new ValidationException("the category description wasn't informed.");
        }
    }
}
