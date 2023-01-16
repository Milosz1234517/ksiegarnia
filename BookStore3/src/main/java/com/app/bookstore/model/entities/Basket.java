package com.app.bookstore.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "basket", schema = "public", catalog = "BookStore")
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private int itemId;

    @ManyToOne(targetEntity = BookHeader.class)
    @JoinColumn(name = "book_header_id")
    private BookHeader bookHeader;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    @Basic
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
