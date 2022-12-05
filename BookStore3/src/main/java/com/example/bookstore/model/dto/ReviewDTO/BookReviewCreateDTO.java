package com.example.bookstore.model.dto.ReviewDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BookReviewCreateDTO {

    private String description;

    @Min(value = 1, message = "Mark must be greater than 0")
    @Max(value = 10, message = "Mark must be not higher than 10")
    private Integer mark;

    @NotNull(message = "Book header must be added")
    private Integer bookHeaderId;
}
