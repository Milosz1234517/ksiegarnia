package com.example.bookstore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false)
    private int authorId;

    @Basic
    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Basic
    @Column(name = "surname", length = 15)
    private String surname;
}
