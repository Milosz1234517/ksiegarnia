package com.example.bookstore.model.dto.BookHeaderDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookHeaderIdDTO {

    @NotNull(message = "Book header must contain id")
    private Integer bookHeaderId;

}
