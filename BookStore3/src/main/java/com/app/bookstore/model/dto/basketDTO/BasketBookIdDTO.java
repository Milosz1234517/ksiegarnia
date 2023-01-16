package com.app.bookstore.model.dto.basketDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BasketBookIdDTO {

    @NotNull(message = " book header must contain id")
    private Integer bookHeaderId;
}
