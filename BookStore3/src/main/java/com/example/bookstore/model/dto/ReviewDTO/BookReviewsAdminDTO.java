package com.example.bookstore.model.dto.ReviewDTO;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderTitleDTO;
import com.example.bookstore.model.dto.UserDTO.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewsAdminDTO {

    private Integer reviewId;

    private String description;

    private Integer mark;

    private UserDTO user;

    private BookHeaderTitleDTO bookHeader;

}
