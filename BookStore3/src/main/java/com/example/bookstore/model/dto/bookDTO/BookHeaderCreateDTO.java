package com.example.bookstore.model.dto.bookDTO;

import com.example.bookstore.model.dto.categoryDTO.CategoryCreateDTO;
import com.example.bookstore.model.entities.PublishingHouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;


@Getter
@Setter
public class BookHeaderCreateDTO {

    @JsonIgnore
    private Integer bookHeaderId;

    @Valid
    @NotEmpty(message = " book must have category")
    private Collection<CategoryCreateDTO> bookCategories;

    @Valid
    @NotNull(message = " book must have release date")
    private Date releaseDate;

    @Valid
    @NotNull(message = " book must have edition")
    private Integer edition;

    @Valid
    @NotNull(message = " book must have publishing house")
    private PublishingHouse publishingHouse;

    private String description;

    @Valid
    @NotBlank(message = " title cannot be empty")
    private String bookTitle;

    @Valid
    @NotEmpty(message = " book must have author")
    private Collection<AuthorDTO> bookAuthors;

    private String icon;

    @Valid
    @Min(value = 1, message = " quantity must be greater than 0")
    private Integer quantity;

    @Valid
    @NotNull(message = " enter some price")
    private BigDecimal price;

}
