package com.example.bookstore.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
class OrderBookKey implements Serializable {

    @Column(name = "order_header_id", nullable = false)
    Long orderHeaderId;


    @Column(name = "book_header_id", nullable = false)
    Integer bookHeaderId;

}
