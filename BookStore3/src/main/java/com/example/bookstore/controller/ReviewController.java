package com.example.bookstore.controller;

import com.example.bookstore.model.dto.ReviewDTO.BookReviewCreateDTO;
import com.example.bookstore.model.dto.ReviewDTO.BookReviewUpdateDTO;
import com.example.bookstore.model.dto.ReviewDTO.BookReviewsAdminDTO;
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

    @GetMapping("/checkReviewPossibility")
    public Boolean checkReviewPossibility(@RequestParam Integer bookHeaderId, @RequestParam Long orderId, HttpServletRequest request){
        return reviewService.checkReviewPossibility(bookHeaderId, orderId, request);
    }

    @GetMapping("/getReviewsForBookCount")
    public long getReviewsForBook(@RequestParam Integer bookHeaderId){
        return reviewService.getReviewsForBookCount(bookHeaderId);
    }

    @GetMapping("/getReviewsForUser")
    public List<BookReviewsDTO> getReviewsForUser(@RequestParam Integer page, HttpServletRequest request){
        return reviewService.getReviewsForUser(page, request);
    }

    @GetMapping("/getReviewsForApprove")
    public List<BookReviewsAdminDTO> getReviewsForApprove(@RequestParam Integer page){
        return reviewService.getReviewsForApprove(page);
    }

    @GetMapping("/getReviewsForApproveCount")
    public Long getReviewsForApproveCount(){
        return reviewService.getReviewsForApproveCount();
    }

    @GetMapping("/getReviewsForUserCount")
    public Long getReviewsForUserCount(HttpServletRequest request){
        return reviewService.getReviewsForUserCount(request);
    }

    @PostMapping("/reviewBook")
    public ResponseEntity<?> reviewBook(@Valid @RequestBody BookReviewCreateDTO bookReviewCreateDTO, @RequestParam Long orderId, HttpServletRequest request){
        reviewService.reviewBook(bookReviewCreateDTO, orderId, request);
        return ResponseEntity.ok(new MessageResponse("Book review added successfully"));
    }

    @PutMapping("/modifyReview")
    public ResponseEntity<?> modifyReview(@Valid@RequestBody BookReviewUpdateDTO bookReviewCreateDTO, HttpServletRequest request){
        reviewService.modifyReview(bookReviewCreateDTO, request);
        return ResponseEntity.ok(new MessageResponse("Book review modified successfully"));
    }

    @PutMapping("/approveReview")
    public ResponseEntity<?> approveReview(@RequestParam Integer reviewId){
        reviewService.approveReview(reviewId);
        return ResponseEntity.ok(new MessageResponse("Book review approved successfully"));
    }

    @DeleteMapping("/deleteReview")
    public ResponseEntity<?> deleteReview(@RequestParam Integer reviewId){
        reviewService.delete(reviewId);
        return ResponseEntity.ok(new MessageResponse("Book review deleted successfully"));
    }
}
