package com.example.bookstore.model.dto.ReviewDTO;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDTO;
import com.example.bookstore.model.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewsDTO {

    private Integer reviewId;

    protected String description;

    protected Integer mark;

    protected UserDTO user;

}
