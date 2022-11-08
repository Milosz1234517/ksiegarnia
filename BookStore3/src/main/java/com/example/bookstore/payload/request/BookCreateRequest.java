package com.example.bookstore.payload.request;

import com.example.bookstore.model.dto.BookHeaderCreateDTO;
import com.example.bookstore.model.dto.CategoryDTO;
import com.example.bookstore.model.entities.Author;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.model.entities.PublishingHouse;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class BookCreateRequest {

    private BookHeaderCreateDTO bookHeaderCreateDTO;

    private Collection<Author> authors;

    private Collection<CategoryDTO> categories;

    private PublishingHouse publishingHouse;

}
