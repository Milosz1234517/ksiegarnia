package com.example.bookstore.model.dto;

import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewsDTO {

    private String description;

    private int mark;

    private UserDTO user;

}
