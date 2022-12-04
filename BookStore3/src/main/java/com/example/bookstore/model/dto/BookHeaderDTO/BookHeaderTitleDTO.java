package com.example.bookstore.model.dto.BookHeaderDTO;

import com.example.bookstore.model.entities.Author;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderTitleDTO {

    private String bookTitle;

}
