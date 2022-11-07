package com.example.bookstore.service;

import com.example.bookstore.model.dto.AuthorDTO;
import com.example.bookstore.model.dto.BookHeaderDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookHeaderRepository;
import com.example.bookstore.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookService {

    BookHeaderRepository bookHeaderRepository;
    AuthorRepository authorRepository;

    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setBookHeaderRepository(BookHeaderRepository bookHeaderRepository) {
        this.bookHeaderRepository = bookHeaderRepository;
    }

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<BookHeaderDTO> searchBooksByTitle(String bookTitle, Integer page) {
        return bookHeaderRepository
                .findByBookTitleLikeIgnoreCase(
                        "%" + bookTitle + "%",
                        PageRequest.of(--page, 20)
                )
                .stream()
                .map(bookHeader -> modelMapper.map(bookHeader, BookHeaderDTO.class))
                .collect(Collectors.toList());
    }

    public List<BookHeaderDTO> searchBooksByAuthor(String authorName, String authorSurname, Integer page) {
        throw new RuntimeException();
    }

    public List<BookHeaderDTO> getAllAvailableBooks(Integer page) {
        return bookHeaderRepository
                .findByQuantityGreaterThan(
                        0,
                        PageRequest.of(--page, 20)
                )
                .stream()
                .map(bookHeader -> modelMapper.map(bookHeader, BookHeaderDTO.class))
                .toList();
    }

    public List<BookHeaderDTO> searchBooksByTitleCategory(String title, String category, Integer page) {
        return bookHeaderRepository
                .findByBookTitleLikeIgnoreCaseAndBookCategories_Description(
                        "%" + title + "%",
                        category,
                        PageRequest.of(--page, 20)
                )
                .stream()
                .map(bookHeader -> modelMapper.map(bookHeader, BookHeaderDTO.class))
                .toList();
    }
}
