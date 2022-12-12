package com.example.bookstore.model.dto.reviewDTO;

import com.example.bookstore.model.dto.bookDTO.BookHeaderTitleDTO;
import com.example.bookstore.model.dto.userDTO.UserDTO;
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
