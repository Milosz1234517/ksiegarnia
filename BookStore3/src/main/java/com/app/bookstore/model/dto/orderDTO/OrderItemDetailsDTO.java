package com.app.bookstore.model.dto.orderDTO;

import com.app.bookstore.model.dto.bookDTO.BookHeaderDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDetailsDTO {

    private Long orderId;

    private BigDecimal price;

    private Integer quantity;

    private BookHeaderDTO bookHeader;
}
