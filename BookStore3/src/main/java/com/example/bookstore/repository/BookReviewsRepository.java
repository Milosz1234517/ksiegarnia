package com.example.bookstore.repository;

import com.example.bookstore.model.entities.BookReviews;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookReviewsRepository extends JpaRepository<BookReviews, Integer> {
    List<BookReviews> findByBookHeader_BookHeaderId(Integer bookHeaderId, PageRequest of);

    long countByBookHeader_BookHeaderId(int bookHeaderId);

    List<BookReviews> findByUser_Login(String login, Pageable pageable);

    Optional<BookReviews> findByReviewIdAndUser_Login(Integer reviewId, String login);

    boolean existsByBookHeader_BookHeaderIdAndUser_Login(int bookHeaderId, String login);


}
