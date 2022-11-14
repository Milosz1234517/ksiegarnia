package com.example.bookstore.model.dto.CategoryDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryDTO {

    @JsonIgnore
    private Integer categoryId;

    @NotBlank(message = "Category must have name")
    private String description;

}
