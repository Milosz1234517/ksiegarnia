package com.app.bookstore.model.dto.orderDTO;

import com.app.bookstore.model.dto.bookDTO.BookHeaderIdDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderItemDTO {

    @Valid
    @NotNull(message = " order item must contain book")
    private BookHeaderIdDTO bookHeader;

    @Min(value = 1, message = " quantity must be greater than 0")
    @Max(value = 1000, message = " quantity must be less than 1000")
    private Integer quantity;
}
