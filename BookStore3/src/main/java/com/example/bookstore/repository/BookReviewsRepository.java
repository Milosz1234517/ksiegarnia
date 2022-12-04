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
    List<BookReviews> findByApproveStatus(boolean approveStatus, Pageable pageable);

    long countByApproveStatus(boolean approveStatus);

    List<BookReviews> findByBookHeader_BookHeaderIdAndApproveStatus(int bookHeaderId, boolean approveStatus, Pageable pageable);

    long countByBookHeader_BookHeaderIdAndApproveStatus(int bookHeaderId, boolean approveStatus);

    List<BookReviews> findByUser_LoginAndApproveStatus(String login, Pageable pageable, boolean approve);

    Optional<BookReviews> findByReviewIdAndUser_Login(Integer reviewId, String login);

    boolean existsByBookHeader_BookHeaderIdAndUser_Login(int bookHeaderId, String login);

    long countByUser_LoginAndApproveStatus(String login, boolean approve);

}
