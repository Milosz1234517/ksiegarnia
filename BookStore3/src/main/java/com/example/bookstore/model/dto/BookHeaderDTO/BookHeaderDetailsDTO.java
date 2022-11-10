package com.example.bookstore.model.dto.BookHeaderDTO;

import com.example.bookstore.model.dto.CategoryDTO.CategoryDTO;
import com.example.bookstore.model.entities.PublishingHouse;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderDetailsDTO extends BookHeaderDTO{

    private Collection<CategoryDTO> bookCategories;

    private Date releaseDate;

    private Integer edition;

    private PublishingHouse publishingHouse;

    private String description;

}
