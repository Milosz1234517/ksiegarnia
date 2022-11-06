package com.example.bookstore.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookHeaderDTO {

    private int bookHeaderId;

    private String bookTitle;

    private Integer quantity;

    private String icon;

    private BigDecimal price;

}
