package com.example.bookstore.repository;

import com.example.bookstore.model.entities.BookReviews;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewsRepository extends JpaRepository<BookReviews, Integer> {
}
