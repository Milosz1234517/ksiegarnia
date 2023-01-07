package com.example.bookstore.model.dto.basketDTO;

import com.example.bookstore.model.dto.bookDTO.BookHeaderDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Setter
public class BasketDTO {

    private int itemId;

    private BookHeaderDTO bookHeader;

    private Integer quantity;

    private BigDecimal price;
}
