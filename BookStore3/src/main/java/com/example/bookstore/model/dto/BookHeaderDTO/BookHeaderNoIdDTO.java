package com.example.bookstore.model.dto.BookHeaderDTO;

import com.example.bookstore.model.dto.CategoryDTO;
import com.example.bookstore.model.entities.PublishingHouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Collection;


@Getter
@Setter
public class BookHeaderNoIdDTO extends BookHeaderDTO{

    @JsonIgnore
    private Integer bookHeaderId;

    protected Collection<CategoryDTO> bookCategories;

    protected Date releaseDate;

    protected Integer edition;

    protected PublishingHouse publishingHouse;

    protected String description;

}
