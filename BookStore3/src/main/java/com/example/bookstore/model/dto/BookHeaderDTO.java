package com.example.bookstore.model.dto;

import java.math.BigDecimal;
import java.util.Collection;

import com.example.bookstore.model.entities.Author;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.model.entities.PublishingHouse;
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

    private Collection<AuthorDTO> authors;

    private Collection<Category> bookCategories;

    private PublishingHouse publishingHouse;

}
