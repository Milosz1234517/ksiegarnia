package com.example.bookstore.service;

import com.example.bookstore.model.dto.CategoryDTO.CategoryDTO;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<Category> getAllCategories(Integer page){
        return categoryRepository.findAll(PageRequest.of(--page, 20)).getContent();
    }

    public void addCategory(CategoryDTO categoryIdIgnoreDTO){
        categoryRepository.save(modelMapper.map(categoryIdIgnoreDTO, Category.class));
    }

    public void updateCategory(Category category){
        categoryRepository.save(category);
    }
}
