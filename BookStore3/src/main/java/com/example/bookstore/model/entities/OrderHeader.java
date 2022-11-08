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
@Table(name = "order_header", schema = "public", catalog = "BookStore")
public class OrderHeader {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Basic
    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Basic
    @Column(name = "total_price", nullable = false, precision = 2)
    private BigDecimal totalPrice;

    @Basic
    @Column(name = "item_quantity", nullable = false)
    private int itemQuantity;

    @Basic
    @Column(name = "realization_date", nullable = false)
    private Date realizationDate;

    @Basic
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "orderHeader", cascade = CascadeType.ALL)
    private Collection<Documents> documents;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "ordered_by")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_header_id"))
    private Collection<BookHeader> bookHeaders;

    @ManyToOne(targetEntity = OrderStatus.class)
    @JoinColumn(name = "status_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OrderStatus orderStatus;

}
