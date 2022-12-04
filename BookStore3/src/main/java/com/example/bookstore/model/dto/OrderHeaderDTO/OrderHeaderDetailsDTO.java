package com.example.bookstore.model.dto.OrderHeaderDTO;

import com.example.bookstore.model.dto.OrderItemDetailsDTO;
import com.example.bookstore.model.dto.OrderStatusDTO;
import com.example.bookstore.model.entities.OrderItems;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
public class OrderHeaderDetailsDTO {

    private Long orderId;

    private Timestamp orderDate;

    private BigDecimal totalPrice;

    private Timestamp realizationDate;

    private String description;

    private OrderStatusDTO orderStatus;

    private Collection<OrderItemDetailsDTO> orderItems;

}
