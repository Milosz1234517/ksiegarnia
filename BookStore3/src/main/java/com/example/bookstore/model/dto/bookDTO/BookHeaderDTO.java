package com.example.bookstore.model.dto.bookDTO;

import com.example.bookstore.model.entities.Author;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderDTO {

    private Integer bookHeaderId;

    private String bookTitle;

    private Collection<Author> bookAuthors;

    private String icon;

    private Integer quantity;

    private BigDecimal price;

}
