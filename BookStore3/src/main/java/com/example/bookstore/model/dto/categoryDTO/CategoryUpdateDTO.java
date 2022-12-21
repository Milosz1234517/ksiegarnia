package com.example.bookstore.model.dto.categoryDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoryUpdateDTO {

    private Integer categoryId;

    @Valid
    @NotBlank(message = " category must have name")
    @Size(max = 100, message = " category name is too long")
    private String description;

}
