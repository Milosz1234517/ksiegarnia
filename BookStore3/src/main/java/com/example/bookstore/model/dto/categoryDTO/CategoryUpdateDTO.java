package com.example.bookstore.model.dto.categoryDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryUpdateDTO {

    private Integer categoryId;

    @Valid
    @NotBlank(message = " category must have name")
    private String description;

}
