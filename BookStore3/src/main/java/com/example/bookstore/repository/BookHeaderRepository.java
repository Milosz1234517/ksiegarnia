package com.example.bookstore.repository;

import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookHeaderRepository extends JpaRepository<BookHeader, Long> {

    List<BookHeader> findByBookTitleLikeIgnoreCase(
            String bookTitle,
            Pageable pageable
    );

    List<BookHeader> findByQuantityGreaterThan(
            Integer quantity,
            Pageable pageable
    );

    List<BookHeader> findByBookTitleLikeIgnoreCaseAndBookCategories_Description(
            String bookTitle,
            String description,
            Pageable pageable
    );



}

