package com.example.bookstore.model.dto.BookHeaderDTO;

import com.example.bookstore.model.entities.Author;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderDTO extends BookHeaderOnlyIdDTO{

    private String bookTitle;

    private Collection<Author> authors;

    private String icon;

    private Integer quantity;

    private BigDecimal price;

}
