package com.example.bookstore.model.dto.bookDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthorDTO {

    @JsonIgnore
    private int authorId;

    @Valid
    @NotBlank(message = " author must have name")
    private String name;

    @Valid
    @NotBlank(message = " author must have surname")
    private String surname;
}
