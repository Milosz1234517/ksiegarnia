package com.example.bookstore.model.dto.ReviewDTO;

import com.example.bookstore.model.dto.UserDTO.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewsDTO {

    private Integer reviewId;

    private String description;

    private Integer mark;

    private UserDTO user;

}
