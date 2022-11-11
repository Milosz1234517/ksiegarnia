package com.example.bookstore.model.dto.OrderHeaderDTO;

import com.example.bookstore.model.dto.OrderStatusDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class OrderHeaderDetailsDTO {

    private Long orderId;

    private Date orderDate;

    private BigDecimal totalPrice;

    private Date realizationDate;

    private String description;

    private OrderStatusDTO orderStatus;

}
