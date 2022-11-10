package com.example.bookstore.service;

import com.example.bookstore.model.dto.ReviewDTO.BookReviewsDTO;
import com.example.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final BookReviewsRepository bookReviewsRepository;
    private final ModelMapper modelMapper;

    public List<BookReviewsDTO> getReviewsForBook(Integer page, Integer bookHeaderId){
        return bookReviewsRepository
                .findByBookHeader_BookHeaderId(bookHeaderId, PageRequest.of(--page, 20))
                .stream()
                .map(bookReviews -> modelMapper.map(bookReviews, BookReviewsDTO.class))
                .toList();
    }

    public List<BookReviewsDTO> getReviewsForUser(Integer page, String login) {
        return bookReviewsRepository
                .findByUser_Login(login, PageRequest.of(--page, 20))
                .stream()
                .map(bookReviews -> modelMapper.map(bookReviews, BookReviewsDTO.class))
                .toList();
    }
}
