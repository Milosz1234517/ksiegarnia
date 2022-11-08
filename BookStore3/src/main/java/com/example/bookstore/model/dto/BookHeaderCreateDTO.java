package com.example.bookstore.model.dto;

import com.example.bookstore.model.entities.Author;
import com.example.bookstore.model.entities.PublishingHouse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.sql.Date;
import java.util.Collection;

@Getter
@Setter
public class BookHeaderCreateDTO {

    private String bookTitle;

    private String icon;

    private Date releaseDate;

    private Integer edition;

    private String description;

}
