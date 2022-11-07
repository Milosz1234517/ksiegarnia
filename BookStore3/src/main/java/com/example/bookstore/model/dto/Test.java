package com.example.bookstore.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Test {

    String name;
    String surname;
    Integer page;
}
