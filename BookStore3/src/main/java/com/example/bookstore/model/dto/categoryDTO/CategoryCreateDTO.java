package com.example.bookstore.model.dto.categoryDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryCreateDTO {

    @JsonIgnore
    private Integer categoryId;

    @Valid
    @NotBlank(message = " category must have name")
    private String description;

}
