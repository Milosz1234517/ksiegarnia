package com.example.bookstore.model.dto.OrderHeaderDTO;

import com.example.bookstore.model.dto.OrderItemDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Getter
@Setter
public class OrderHeaderDTO {

    @Valid
    @NotEmpty(message = "Order must contain items")
    private Collection<OrderItemDTO> orderItems;

    private String description;
}
