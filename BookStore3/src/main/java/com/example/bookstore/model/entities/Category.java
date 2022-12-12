package com.example.bookstore.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name = "category", schema = "public", catalog = "BookStore")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Basic
    @Column(name = "description", unique = true)
    private String description;

}