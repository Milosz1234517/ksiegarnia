package com.example.bookstore.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthorDTO {

    private int authorId;

    @Valid
    @NotBlank(message = "Author must have name")
    private String name;

    @Valid
    @NotBlank(message = "Author must have surname")
    private String surname;
}
