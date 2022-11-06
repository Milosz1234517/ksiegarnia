package com.example.bookstore.service;

import com.example.bookstore.model.dto.BookHeaderDTO;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookHeaderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookService {

    BookHeaderRepository bookHeaderRepository;
    AuthorRepository authorRepository;
    ModelMapper modelMapper;

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
                .findByBookTitleLikeIgnoreCaseAndQuantityGreaterThan(
                        "%" + bookTitle + "%",
                        0,
                        PageRequest.of(--page, 2)
                )
                .stream()
                .map(bookHeader -> modelMapper.map(bookHeader, BookHeaderDTO.class))
                .collect(Collectors.toList());
    }

    public List<BookHeaderDTO> getHomepageBooks(Integer page) {
        return bookHeaderRepository
                .findByQuantityGreaterThan(
                        0,
                        PageRequest.of(--page, 2)
                )
                .stream()
                .map(bookHeader -> modelMapper.map(bookHeader, BookHeaderDTO.class))
                .collect(Collectors.toList());
    }

}
