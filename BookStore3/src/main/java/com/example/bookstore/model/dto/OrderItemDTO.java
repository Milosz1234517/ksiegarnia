package com.example.bookstore.model.dto;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderOnlyIdDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

    private BookHeaderOnlyIdDTO bookHeader;

    private Integer quantity;
}
