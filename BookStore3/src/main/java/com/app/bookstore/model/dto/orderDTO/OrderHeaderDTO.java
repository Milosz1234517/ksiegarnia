package com.app.bookstore.model.dto.orderDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

@Getter
@Setter
public class OrderHeaderDTO {

    @Valid
    @NotEmpty(message = " order must contain items")
    private Collection<OrderItemDTO> orderItems;

    @Valid
    @Size(max = 30000, message = " description too long")
    private String description;
}
