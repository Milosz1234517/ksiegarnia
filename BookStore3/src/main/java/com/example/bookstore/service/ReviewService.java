package com.example.bookstore.service;

import com.example.bookstore.jwt.JwtUtils;
import com.example.bookstore.model.dto.ReviewDTO.BookReviewCreateDTO;
import com.example.bookstore.model.dto.ReviewDTO.BookReviewUpdateDTO;
import com.example.bookstore.model.dto.ReviewDTO.BookReviewsDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.BookReviews;
import com.example.bookstore.model.entities.Users;
import com.example.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final BookReviewsRepository bookReviewsRepository;
    private final OrderHeaderRepository orderHeaderRepository;
    private final UserRepository userRepository;
    private final BookHeaderRepository bookHeaderRepository;
    private final JwtUtils jwtUtils;
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

    public void reviewBook(BookReviewCreateDTO bookReviewCreateDTO, HttpServletRequest request){
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        BookHeader bookHeader = bookHeaderRepository.findByBookHeaderId(bookReviewCreateDTO.getBookHeaderId()).orElseThrow();

        if(!orderHeaderRepository.existsByOrderItems_BookHeader_BookHeaderIdAndUser_Login(bookHeader.getBookHeaderId(), user.getLogin()))
            throw new RuntimeException();

        if(bookReviewsRepository.existsByBookHeader_BookHeaderIdAndUser_Login(bookHeader.getBookHeaderId(), user.getLogin()))
            throw new RuntimeException();

        BookReviews bookReviews = new BookReviews();
        bookReviews.setUser(user);
        bookReviews.setBookHeader(bookHeader);
        bookReviews.setMark(bookReviewCreateDTO.getMark());
        bookReviews.setDescription(bookReviewCreateDTO.getDescription());

        bookReviewsRepository.save(bookReviews);
    }

    public void modifyReview(BookReviewUpdateDTO bookReviewUpdateDTO,  HttpServletRequest request){
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        BookReviews bookReviews = bookReviewsRepository.findByReviewIdAndUser_Login(bookReviewUpdateDTO.getReviewId(), users.getLogin()).orElseThrow();
        bookReviews.setDescription(bookReviewUpdateDTO.getDescription());
        bookReviews.setMark(bookReviewUpdateDTO.getMark());
        bookReviewsRepository.save(bookReviews);
    }

    public void delete(Integer reviewID){
        BookReviews bookReviews = bookReviewsRepository.findById(reviewID).orElseThrow();
        bookReviewsRepository.delete(bookReviews);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
