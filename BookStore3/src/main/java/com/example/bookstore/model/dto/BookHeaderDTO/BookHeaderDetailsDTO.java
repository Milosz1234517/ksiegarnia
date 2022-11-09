package com.example.bookstore.model.dto.BookHeaderDTO;

import com.example.bookstore.model.dto.BookReviewsDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;


@Getter
@Setter
public class BookHeaderDetailsDTO extends BookHeaderNoIdDTO{

    private Integer bookHeaderId;

    private Collection<BookReviewsDTO> bookReviews;

}
