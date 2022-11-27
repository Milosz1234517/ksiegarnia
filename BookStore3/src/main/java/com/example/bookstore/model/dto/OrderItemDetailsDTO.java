package com.example.bookstore.model.dto;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.OrderHeader;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDetailsDTO {

    private BigDecimal price;

    private Integer quantity;

    private BookHeaderDTO bookHeader;
}
