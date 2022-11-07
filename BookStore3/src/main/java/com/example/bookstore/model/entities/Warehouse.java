package com.example.bookstore.model.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Data
@Table(name = "warehouse", schema = "public", catalog = "BookStore")
public class Warehouse {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    @Basic
    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private BigDecimal price;

    @OneToOne
    @JoinColumn(name = "book_header_id", referencedColumnName = "book_header_id")
    private BookHeader bookHeader;


    public Warehouse() {

    }
}
