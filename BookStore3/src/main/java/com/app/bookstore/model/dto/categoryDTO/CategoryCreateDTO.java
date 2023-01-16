package com.app.bookstore.model.dto.categoryDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoryCreateDTO {

    @JsonIgnore
    private Integer categoryId;

    @Valid
    @NotBlank( message = " category must have name")
    @Size(max = 100, message = " category name is too long")
    private String description;

}
