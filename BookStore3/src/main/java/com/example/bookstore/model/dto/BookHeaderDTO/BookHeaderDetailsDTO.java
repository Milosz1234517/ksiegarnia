package com.example.bookstore.model.dto.BookHeaderDTO;

import com.example.bookstore.model.dto.CategoryDTO.CategoryDTO;
import com.example.bookstore.model.entities.PublishingHouse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderDetailsDTO extends BookHeaderDTO{

    @Valid
    @NotEmpty(message = "Book must have category")
    private Collection<CategoryDTO> bookCategories;

    @Valid
    @NotNull(message = "Book must have release date")
    private Date releaseDate;

    @Valid
    @NotNull(message = "Book must have edition")
    private Integer edition;

    @Valid
    @NotNull(message = "Book must have publishing house")
    private PublishingHouse publishingHouse;

    private String description;

}
