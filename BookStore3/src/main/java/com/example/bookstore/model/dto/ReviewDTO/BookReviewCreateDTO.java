package com.example.bookstore.model.dto.ReviewDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewCreateDTO {

    private String description;

    private Integer mark;

    private Integer bookHeaderId;

    private Long orderId;
}
