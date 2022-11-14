package com.example.bookstore.model.dto;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderOnlyIdDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderItemDTO {

    @Valid
    @NotNull(message = "Order item must contain book")
    private BookHeaderOnlyIdDTO bookHeader;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}
