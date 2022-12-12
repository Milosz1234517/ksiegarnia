package com.example.bookstore.model.dto.reviewDTO;

import com.example.bookstore.model.dto.bookDTO.BookHeaderTitleDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewsDTO {

    private Integer reviewId;

    private String description;

    private Integer mark;

    private BookHeaderTitleDTO bookHeader;

}
