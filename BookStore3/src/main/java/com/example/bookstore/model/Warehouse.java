package com.example.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Warehouse {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "item_id", nullable = false)
    private int itemId;

    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(targetEntity = BookHeader.class)
    @JoinColumn(name = "book_header_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BookHeader bookHeader;

}
