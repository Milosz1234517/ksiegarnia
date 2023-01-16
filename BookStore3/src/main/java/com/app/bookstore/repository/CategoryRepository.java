package com.app.bookstore.repository;

import com.app.bookstore.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByDescriptionIgnoreCase(String description);

}
