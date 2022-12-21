package com.example.bookstore.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "book_header", schema = "public", catalog = "BookStore")
public class BookHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_header_id", nullable = false)
    private int bookHeaderId;

    @Basic
    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "release_date")
    private Date releaseDate;

    @Basic
    @Column(name = "edition")
    private Integer edition;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "icon", length = 2048)
    private String icon;

    @Basic
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Basic
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;


    @ManyToMany()
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_header_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Collection<Author> bookAuthors;

    @ManyToMany()
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_header_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Collection<Category> bookCategories;

    @ManyToOne(targetEntity = PublishingHouse.class)
    @JoinColumn(name = "publishing_house_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PublishingHouse publishingHouse;
}
