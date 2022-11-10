package com.example.bookstore.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class OrderHeaderDTO {

    private Collection<OrderItemDTO> orderItems;

    private UserDTO user;

    private String description;
}
