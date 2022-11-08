package com.example.bookstore.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "category", schema = "public", catalog = "BookStore")
public class Category {

    @Id
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToMany()
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "book_header_id"))
    private Collection<BookHeader> bookHeaders;

}