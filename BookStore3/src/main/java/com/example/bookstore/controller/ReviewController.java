package com.example.bookstore.controller;

import com.example.bookstore.model.dto.ReviewDTO.BookReviewCreateDTO;
import com.example.bookstore.model.dto.ReviewDTO.BookReviewUpdateDTO;
import com.example.bookstore.model.dto.ReviewDTO.BookReviewsDTO;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/getReviewsForBook")
    public List<BookReviewsDTO> getReviewsForBook(@RequestParam Integer page, @RequestParam Integer bookHeaderId){
        return reviewService.getReviewsForBook(page, bookHeaderId);
    }

    @GetMapping("/getReviewsForUser")
    public List<BookReviewsDTO> getReviewsForUser(@RequestParam Integer page, @RequestParam String login){
        return reviewService.getReviewsForUser(page, login);
    }

    @PostMapping("/reviewBook")
    public ResponseEntity<?> reviewBook(@Valid @RequestBody BookReviewCreateDTO bookReviewCreateDTO, HttpServletRequest request){
        reviewService.reviewBook(bookReviewCreateDTO, request);
        return ResponseEntity.ok(new MessageResponse("Book review added successfully"));
    }

    @PutMapping("/modifyReview")
    public ResponseEntity<?> modifyReview(@Valid@RequestBody BookReviewUpdateDTO bookReviewCreateDTO, HttpServletRequest request){
        reviewService.modifyReview(bookReviewCreateDTO, request);
        return ResponseEntity.ok(new MessageResponse("Book review modified successfully"));
    }

    @DeleteMapping("/deleteReview")
    public ResponseEntity<?> deleteReview(@RequestParam Integer reviewId){
        reviewService.delete(reviewId);
        return ResponseEntity.ok(new MessageResponse("Book review deleted successfully"));
    }
}
