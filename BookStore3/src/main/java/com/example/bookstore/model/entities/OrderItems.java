package com.example.bookstore.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Table(name = "order_items", schema = "public", catalog = "BookStore")
public class OrderItems {

    @EmbeddedId
    OrderBookKey id;

    @Basic
    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @MapsId("bookHeaderId")
    @JoinColumn(name = "book_header_id")
    private BookHeader bookHeaders;

    @ManyToOne
    @MapsId("orderHeaderId")
    @JoinColumn(name = "order_header_id")
    @JsonBackReference
    private OrderHeader orderHeader;

}

