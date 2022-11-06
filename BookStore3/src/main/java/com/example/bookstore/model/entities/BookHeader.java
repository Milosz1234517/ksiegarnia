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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "book_header_id", nullable = false)
    private int bookHeaderId;

    @Basic
    @Column(name = "book_title", nullable = false, length = 255)
    private String bookTitle;

    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "release_date")
    private Date releaseDate;

    @Basic
    @Column(name = "edition")
    private Integer edition;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "icon", length = 15)
    private String icon;

    @Basic
    @Column(name = "price", nullable = false, precision = 2)
    private BigDecimal price;


    @ManyToMany()
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_header_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Collection<Author> bookAuthors;

    @ManyToOne(targetEntity = PublishingHouse.class)
    @JoinColumn(name = "publishing_house_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PublishingHouse publishingHouse;

    @OneToMany(mappedBy = "bookHeader", cascade = CascadeType.ALL)
    private Collection<BookReviews> bookReviews;

}
