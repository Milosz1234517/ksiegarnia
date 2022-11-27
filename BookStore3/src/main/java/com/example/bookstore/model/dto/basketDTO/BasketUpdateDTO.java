package com.example.bookstore.model.dto.basketDTO;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDTO;
import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderOnlyIdDTO;
import com.example.bookstore.model.entities.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketUpdateDTO {

    private BookHeaderOnlyIdDTO bookHeader;

    private Integer quantity;
}
