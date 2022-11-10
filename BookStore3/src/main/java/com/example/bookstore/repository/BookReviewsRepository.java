package com.example.bookstore.repository;

import com.example.bookstore.model.entities.BookReviews;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewsRepository extends JpaRepository<BookReviews, Integer> {
    List<BookReviews> findByBookHeader_BookHeaderId(Integer bookHeaderId, PageRequest of);

    List<BookReviews> findByUser_Login(String login, Pageable pageable);


}
