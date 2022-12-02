package com.example.bookstore.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthorDTO {

    private int authorId;

    @Valid
    @NotBlank(message = "Author must have name")
    private String name;

    private String surname;
}
