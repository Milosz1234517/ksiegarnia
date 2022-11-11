package com.example.bookstore.model.dto.ReviewDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewUpdateDTO {

    private Integer reviewId;

    private String description;

    private Integer mark;

}
