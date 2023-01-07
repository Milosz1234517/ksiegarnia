package com.example.bookstore.payload.response;

import com.example.bookstore.model.dto.basketDTO.BasketDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BasketTotalResponse {
    private List<BasketDTO> basket;
    private BigDecimal totalPrice = new BigDecimal(0);
}
