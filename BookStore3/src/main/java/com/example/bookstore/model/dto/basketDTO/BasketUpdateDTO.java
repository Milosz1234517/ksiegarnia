package com.example.bookstore.model.dto.basketDTO;

import com.example.bookstore.model.dto.bookDTO.BookHeaderIdDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BasketUpdateDTO {

    @Valid
    @NotNull(message = " book header must contain id")
    private BookHeaderIdDTO bookHeader;

    @Min(value = 0, message = " wrong quantity")
    private Integer quantity;
}
