package com.app.bookstore.service;

import com.app.bookstore.model.dto.categoryDTO.CategoryUpdateDTO;
import com.app.bookstore.repository.CategoryRepository;
import com.app.bookstore.model.dto.categoryDTO.CategoryCreateDTO;
import com.app.bookstore.model.entities.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public void addCategory(CategoryCreateDTO categoryIdIgnoreDTO){
        categoryRepository.save(modelMapper.map(categoryIdIgnoreDTO, Category.class));
    }

    public void updateCategory(CategoryUpdateDTO category){
        categoryRepository.save(modelMapper.map(category, Category.class));
    }
}
