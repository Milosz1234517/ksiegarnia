package com.example.bookstore.model.dto;

import java.math.BigDecimal;
import java.sql.Date;
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

    private Collection<Author> authors;

    private Collection<CategoryDTO> bookCategories;

    private String icon;

    private Date releaseDate;

    private Integer edition;

    private PublishingHouse publishingHouse;

    private String description;

}
