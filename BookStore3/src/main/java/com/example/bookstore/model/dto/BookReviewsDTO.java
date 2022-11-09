package com.example.bookstore.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewsDTO {

    private String description;

    private Integer mark;

    private UserDTO user;

}
