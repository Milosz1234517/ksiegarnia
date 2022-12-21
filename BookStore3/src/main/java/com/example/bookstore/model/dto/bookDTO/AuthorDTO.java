package com.example.bookstore.model.dto.bookDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthorDTO {

    @JsonIgnore
    private int authorId;

    @Valid
    @NotBlank(message = " author must have name")
    @Size(max = 100, message = "name too long")
    private String name;

    @Valid
    @NotBlank(message = " author must have surname")
    @Size(max = 100, message = "name too long")
    private String surname;
}
