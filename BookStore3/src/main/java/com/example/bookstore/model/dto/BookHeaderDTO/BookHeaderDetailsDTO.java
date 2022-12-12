package com.example.bookstore.model.dto.BookHeaderDTO;

import com.example.bookstore.model.dto.AuthorDTO;
import com.example.bookstore.model.dto.CategoryDTO.CategoryDTO;
import com.example.bookstore.model.entities.PublishingHouse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderDetailsDTO {

    private int bookHeaderId;

    private String bookTitle;

    private Date releaseDate;

    private Integer edition;

    private String description;

    private String icon;

    private Integer quantity;

    private BigDecimal price;

    private Collection<AuthorDTO> bookAuthors;

    private Collection<CategoryDTO> bookCategories;

    private PublishingHouse publishingHouse;

}
