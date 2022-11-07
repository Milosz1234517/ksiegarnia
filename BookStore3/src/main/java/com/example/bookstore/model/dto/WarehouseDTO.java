package com.example.bookstore.model.dto;

import com.example.bookstore.model.entities.BookHeader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WarehouseDTO {

    private int bookHeaderId;

    private Integer quantity;

    private BigDecimal price;

    private BookHeaderDTO bookHeader;

}
