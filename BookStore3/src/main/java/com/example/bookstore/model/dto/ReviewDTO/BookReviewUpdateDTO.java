package com.example.bookstore.model.dto.ReviewDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookReviewUpdateDTO {

    @NotNull(message = "Review ID cannot be empty")
    private Integer reviewId;

    private String description;

    @Min(value = 1, message = "Mark must be greater than 0")
    @Max(value = 10, message = "Mark must be not higher than 10")
    private Integer mark;

}
