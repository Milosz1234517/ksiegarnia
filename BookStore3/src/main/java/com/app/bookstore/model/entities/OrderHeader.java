package com.app.bookstore.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "order_header", schema = "public", catalog = "BookStore")
public class OrderHeader {

    @Id
    @Column(name = "order_id", nullable = false)
    @SequenceGenerator(name = "ord_seq", sequenceName = "order_seq", initialValue = 100000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ord_seq")
    private Long orderId;

    @Basic
    @Column(name = "order_date", nullable = false)
    private Timestamp orderDate;

    @Basic
    @Valid
    @DecimalMax(value = "10000000000000.00", message = " wrong price value")
    @Column(name = "total_price", nullable = false, precision = 20, scale = 2)
    private BigDecimal totalPrice;

    @Basic
    @Column(name = "realization_date")
    private Timestamp realizationDate;

    @Basic
    @Column(name = "description", length = 30000)
    private String description;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "ordered_by")
    private Users user;
    @ManyToOne(targetEntity = OrderStatus.class)
    @JoinColumn(name = "status_id")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "orderHeader", cascade = CascadeType.ALL)
    private Collection<OrderItems> orderItems;

}
