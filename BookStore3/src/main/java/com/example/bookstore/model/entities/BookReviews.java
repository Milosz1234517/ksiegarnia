package com.example.bookstore.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "book_reviews", schema = "public", catalog = "BookStore")
public class BookReviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    @JsonBackReference
    private Integer reviewId;

    @Basic
    @Column(name = "description", length = 30000)
    private String description;

    @Basic
    @Column(name = "mark", nullable = false)
    private int mark;

    @Basic
    @Column(name = "approve_status", nullable = false)
    private boolean approveStatus;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(targetEntity = BookHeader.class)
    @JoinColumn(name = "book_header_id")
    private BookHeader bookHeader;

}
