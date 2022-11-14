package com.example.bookstore.model.dto.BookHeaderDTO;

import com.example.bookstore.model.entities.Author;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderDTO extends BookHeaderOnlyIdDTO{

    @Valid
    @NotBlank(message = "Title cannot be empty")
    private String bookTitle;

    @Valid
    @NotEmpty(message = "Book must have autor")
    private Collection<Author> authors;

    private String icon;

    @Valid
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @Valid
    @NotNull(message = "Enter some price")
    private BigDecimal price;

}
