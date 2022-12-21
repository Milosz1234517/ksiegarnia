package com.example.bookstore.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "order_status", schema = "public", catalog = "BookStore")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Basic
    @Column(name = "description", unique = true, length = 100)
    private String description;

    @OneToMany(mappedBy = "orderStatus", cascade = CascadeType.ALL)
    private Collection<OrderHeader> orderHeaders;

}
