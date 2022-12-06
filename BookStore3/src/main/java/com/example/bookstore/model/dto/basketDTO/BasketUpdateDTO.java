package com.example.bookstore.model.dto.basketDTO;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDTO;
import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderIdNotNullDTO;
import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderOnlyIdDTO;
import com.example.bookstore.model.entities.Users;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BasketUpdateDTO {

    @Valid
    @NotNull(message = " book header must contain id")
    private BookHeaderIdNotNullDTO bookHeader;

    @Min(value = 0, message = " wrong quantity")
    private Integer quantity;
}
