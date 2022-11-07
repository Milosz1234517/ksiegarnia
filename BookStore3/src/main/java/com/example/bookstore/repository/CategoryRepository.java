package com.example.bookstore.repository;

import com.example.bookstore.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByDescription(String description);

}
