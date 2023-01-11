package com.example.bookstore.service;

import com.example.bookstore.exceptions.BadRequestException;
import com.example.bookstore.jwt.AuthTokenFilter;
import com.example.bookstore.jwt.JwtUtils;
import com.example.bookstore.model.dto.reviewDTO.BookReviewCreateDTO;
import com.example.bookstore.model.dto.reviewDTO.BookReviewUpdateDTO;
import com.example.bookstore.model.dto.reviewDTO.BookReviewsAdminDTO;
import com.example.bookstore.model.dto.reviewDTO.BookReviewsDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.BookReviews;
import com.example.bookstore.model.entities.Users;
import com.example.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public Boolean checkReviewPossibility(Integer bookHeaderId, HttpServletRequest request) {
        return !orderHeaderRepository
                .existsByUser_LoginAndOrderStatus_StatusId(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request)), 3)
                || bookReviewsRepository
                .existsByBookHeader_BookHeaderIdAndUser_Login(bookHeaderId, jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request)));
    }

    public List<BookReviewsDTO> getReviewsForBook(Integer page, Integer bookHeaderId) {
        return bookReviewsRepository
                .findByBookHeader_BookHeaderIdAndApproveStatus(bookHeaderId, true, PageRequest.of(--page, 20))
                .stream()
                .map(bookReviews -> modelMapper.map(bookReviews, BookReviewsDTO.class))
                .toList();
    }

    public long getReviewsForBookCount(Integer bookHeaderId) {
        return bookReviewsRepository
                .countByBookHeader_BookHeaderIdAndApproveStatus(bookHeaderId, true);
    }

    public List<BookReviewsDTO> getReviewsForUser(Integer page, HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request)))
                .orElseThrow(() -> new BadRequestException("User not found"));
        return bookReviewsRepository
                .findByUser_LoginAndApproveStatus(user.getLogin(), PageRequest.of(--page, 20), true)
                .stream()
                .map(bookReviews -> modelMapper.map(bookReviews, BookReviewsDTO.class))
                .toList();
    }

    public Long getReviewsForUserCount(HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request)))
                .orElseThrow(() -> new BadRequestException("User not found"));
        return bookReviewsRepository
                .countByUser_LoginAndApproveStatus(user.getLogin(), true);
    }

    public void reviewBook(BookReviewCreateDTO bookReviewCreateDTO, HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request)))
                .orElseThrow(() -> new BadRequestException("User not found"));
        BookHeader bookHeader = bookHeaderRepository.findByBookHeaderId(bookReviewCreateDTO.getBookHeaderId())
                .orElseThrow(() -> new BadRequestException("Book not found"));

        if (!orderHeaderRepository.existsByOrderItems_BookHeader_BookHeaderIdAndUser_Login(bookHeader.getBookHeaderId(), user.getLogin()))
            throw new BadRequestException("Order not found");

        if (checkReviewPossibility(bookHeader.getBookHeaderId(), request))
            throw new BadRequestException("Cannot create review, because order is not completed or this book has already been reviewed");

        BookReviews bookReviews = new BookReviews();
        bookReviews.setUser(user);
        bookReviews.setBookHeader(bookHeader);
        bookReviews.setMark(bookReviewCreateDTO.getMark());
        bookReviews.setDescription(bookReviewCreateDTO.getDescription());
        bookReviews.setApproveStatus(false);

        bookReviewsRepository.save(bookReviews);
    }

    public void modifyReview(BookReviewUpdateDTO bookReviewUpdateDTO, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request)))
                .orElseThrow(() -> new BadRequestException("User not found"));
        BookReviews bookReviews = bookReviewsRepository.findByReviewIdAndUser_Login(bookReviewUpdateDTO.getReviewId(), users.getLogin())
                .orElseThrow(() -> new BadRequestException("Review not found"));
        bookReviews.setDescription(bookReviewUpdateDTO.getDescription());
        bookReviews.setMark(bookReviewUpdateDTO.getMark());
        bookReviewsRepository.save(bookReviews);
    }

    public void delete(Integer reviewID) {
        BookReviews bookReviews = bookReviewsRepository.findById(reviewID)
                .orElseThrow(() -> new BadRequestException("Review not found"));
        bookReviewsRepository.delete(bookReviews);
    }

    public List<BookReviewsAdminDTO> getReviewsForApprove(Integer page) {
        return bookReviewsRepository
                .findByApproveStatus(false, PageRequest.of(--page, 2))
                .stream()
                .map(bookReviews -> modelMapper.map(bookReviews, BookReviewsAdminDTO.class))
                .toList();
    }

    public Long getReviewsForApproveCount() {
        return bookReviewsRepository
                .countByApproveStatus(false);
    }

    public void approveReview(Integer reviewId) {
        BookReviews bookReviews = bookReviewsRepository.findById(reviewId)
                .orElseThrow(() -> new BadRequestException("Review not found"));
        bookReviews.setApproveStatus(true);
        bookReviewsRepository.save(bookReviews);
    }
}
