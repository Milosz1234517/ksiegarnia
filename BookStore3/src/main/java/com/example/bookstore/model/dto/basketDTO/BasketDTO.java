package com.example.bookstore.model.dto.basketDTO;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDTO;
import com.example.bookstore.model.dto.UserDTO.UserDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class BasketDTO {

    private int itemId;

    private BookHeaderDTO bookHeader;

    private Integer quantity;
}
