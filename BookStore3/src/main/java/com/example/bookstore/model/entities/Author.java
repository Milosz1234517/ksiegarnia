package com.example.bookstore.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false)
    @JsonIgnore
    private int authorId;

    @Basic
    @Valid
    @NotBlank(message = "Author must have name")
    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Basic
    @Column(name = "surname", length = 15)
    private String surname;
}
