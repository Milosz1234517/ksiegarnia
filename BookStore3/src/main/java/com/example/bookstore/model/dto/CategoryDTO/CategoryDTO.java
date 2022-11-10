package com.example.bookstore.model.dto.CategoryDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    @JsonIgnore
    private Integer categoryId;

    private String description;

}
