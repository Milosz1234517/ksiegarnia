package com.example.bookstore.model.dto.basketDTO;

import com.example.bookstore.model.dto.bookDTO.BookHeaderDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketDTO {

    private int itemId;

    private BookHeaderDTO bookHeader;

    private Integer quantity;
}
