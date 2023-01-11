package com.example.bookstore.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Table(name = "order_items", schema = "public", catalog = "BookStore")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Basic
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Basic
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "book_header_id")
    private BookHeader bookHeader;

    @ManyToOne
    @JoinColumn(name = "order_header_id")
    private OrderHeader orderHeader;

}

