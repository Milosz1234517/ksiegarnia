package com.app.bookstore.model.dto.basketDTO;

import com.app.bookstore.model.dto.bookDTO.BookHeaderDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BasketDTO {

    private int itemId;

    private BookHeaderDTO bookHeader;

    private Integer quantity;

    private BigDecimal price;
}
