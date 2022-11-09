package com.example.bookstore.model.dto.BookHeaderDTO;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

import com.example.bookstore.model.entities.Author;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.model.entities.PublishingHouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;

@Getter
@Setter
public class BookHeaderDTO {

    private Integer bookHeaderId;

    protected String bookTitle;

    protected Collection<Author> authors;

    protected String icon;

    protected Integer quantity;

    protected BigDecimal price;

}
