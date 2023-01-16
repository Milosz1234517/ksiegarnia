package com.app.bookstore.model.dto.bookDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookHeaderIdDTO {

    @NotNull(message = " book header must contain id")
    private Integer bookHeaderId;

}
