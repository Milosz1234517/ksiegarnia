package com.example.bookstore.controller;

import com.example.bookstore.model.dto.ReviewDTO.BookReviewsDTO;
import com.example.bookstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
