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


    @Id
    @Column(name = "order_id", nullable = false)
    @SequenceGenerator(name = "order_seq", sequenceName = "order_sequence", initialValue = 100000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    private Long orderId;

    @Basic
    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Basic
    @Column(name = "total_price", nullable = false, precision = 6, scale = 2)
    private BigDecimal totalPrice;

    @Basic
    @Column(name = "item_quantity", nullable = false)
    private int itemQuantity;

    @Basic
    @Column(name = "realization_date")
    private Date realizationDate;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "ordered_by")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users user;
    @ManyToOne(targetEntity = OrderStatus.class)
    @JoinColumn(name = "status_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "orderHeader", cascade = CascadeType.ALL)
    private Collection<OrderItems> orderItems;

}
