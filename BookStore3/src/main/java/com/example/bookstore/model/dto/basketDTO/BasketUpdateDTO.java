package com.example.bookstore.model.dto.basketDTO;

import com.example.bookstore.model.dto.bookDTO.BookHeaderIdDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class BasketUpdateDTO {

    @Valid
    @NotNull(message = " book header must contain id")
    private BookHeaderIdDTO bookHeader;

    @Min(value = 0, message = " quantity must be greater than 0")
    @Max(value = 1000, message = " quantity must be lower than 1000")
    private Integer quantity;

}
