package com.example.bookstore.model.dto.orderDTO;

import com.example.bookstore.model.dto.bookDTO.BookHeaderIdDTO;
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
    private BookHeaderIdDTO bookHeader;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}
