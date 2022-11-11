package com.example.bookstore.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Table(name = "order_items", schema = "public", catalog = "BookStore")
public class OrderItems {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    Long orderId;

    @Basic
    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private BigDecimal price;

    @Basic
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "book_header_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BookHeader bookHeader;

    @ManyToOne
    @JoinColumn(name = "order_header_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OrderHeader orderHeader;

}

