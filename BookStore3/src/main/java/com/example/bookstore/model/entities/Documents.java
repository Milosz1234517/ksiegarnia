package com.example.bookstore.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
public class Documents {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "document_id", nullable = false)
    private int documentId;

    @Basic
    @Column(name = "document_creation_date", nullable = false)
    private Date documentCreationDate;

    @Basic
    @Column(name = "description")
    private Integer description;

    @ManyToOne(targetEntity = OrderHeader.class)
    @JoinColumn(name = "order_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OrderHeader orderHeader;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "seller_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users user;

}
