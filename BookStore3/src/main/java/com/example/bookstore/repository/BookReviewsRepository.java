package com.example.bookstore.repository;

import com.example.bookstore.model.BookReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewsRepository extends JpaRepository<BookReviews, Integer> {
}
