package com.example.bookstore.model.dto.BookHeaderDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookHeaderDetailsIdIgnoreDTO extends BookHeaderDetailsDTO{

    @JsonIgnore
    private Integer bookHeaderId;

}
