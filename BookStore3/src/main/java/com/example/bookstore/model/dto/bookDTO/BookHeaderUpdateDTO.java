package com.example.bookstore.model.dto.bookDTO;

import com.example.bookstore.model.dto.categoryDTO.CategoryCreateDTO;
import com.example.bookstore.model.entities.PublishingHouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderUpdateDTO {

    @Valid
    @NotNull(message = " book header must contain id")
    private Integer bookHeaderId;

    @Valid
    @NotEmpty(message = " book must have category")
    private Collection<CategoryCreateDTO> bookCategories;

    @Valid
    @NotNull(message = " book must have release date")
    private Date releaseDate;

    @Valid
    @NotNull(message = " book must have edition")
    @Min(value = 0, message = " wrong edition")
    @Max(value = 1000, message = " wrong edition")
    private Integer edition;

    @Valid
    @NotNull(message = " book must have publishing house")
    private PublishingHouse publishingHouse;

    @Size(max = 80000, message = " description too long")
    private String description;

    @Valid
    @NotBlank(message = " title cannot be empty")
    @Size(max = 1000, message = " title too long")
    private String bookTitle;

    @Valid
    @NotEmpty(message = " book must have author")
    private Collection<AuthorDTO> bookAuthors;

    @Size(max = 2048, message = " url too long")
    private String icon;

    @Valid
    @Min(value = 1, message = " quantity must be greater than 0")
    @Max(value = 1000000, message = " quantity must be less than 1000000")
    private Integer quantity;

    @Valid
    @NotNull(message = " enter some price")
    @DecimalMax(value = "10000000.00", message = " wrong price value")
    private BigDecimal price;

}
