package com.example.bookstore.controller;

import com.example.bookstore.model.dto.categoryDTO.CategoryCreateDTO;
import com.example.bookstore.model.dto.categoryDTO.CategoryUpdateDTO;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/getCategories")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/addCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryCreateDTO categoryIdIgnoreDTO){
        categoryService.addCategory(categoryIdIgnoreDTO);
        return ResponseEntity.ok(new MessageResponse("Category added successfully"));
    }

    @PutMapping("/updateCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryUpdateDTO category){
        categoryService.updateCategory(category);
        return ResponseEntity.ok(new MessageResponse("Category updated successfully"));
    }

}
