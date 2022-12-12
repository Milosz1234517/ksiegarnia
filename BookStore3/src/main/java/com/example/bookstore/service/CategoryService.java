package com.example.bookstore.service;

import com.example.bookstore.model.dto.categoryDTO.CategoryCreateDTO;
import com.example.bookstore.model.dto.categoryDTO.CategoryUpdateDTO;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.repository.CategoryRepository;
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
