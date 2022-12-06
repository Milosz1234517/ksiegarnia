package com.example.bookstore.model.dto.basketDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BasketBookIdOnlyDTO {

    @NotNull(message = "Book header must contain id")
    private Integer bookHeaderId;
}
