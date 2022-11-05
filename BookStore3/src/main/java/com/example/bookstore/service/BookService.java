package com.example.bookstore.service;

import com.example.bookstore.model.Author;
import com.example.bookstore.model.BookHeader;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;



@Service
public class BookService {

    BookHeaderRepository bookHeaderRepository;
    AuthorRepository authorRepository;

    @Autowired
    public void setBookHeaderRepository(BookHeaderRepository bookHeaderRepository) {
        this.bookHeaderRepository = bookHeaderRepository;
    }

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<BookHeader> getBooks(String bookTitle, String author){
        Author author1 = authorRepository.findAuthorByName(author).get();
        ArrayList<Author> authors = new ArrayList<>();
        authors.add(author1);
        return bookHeaderRepository.findBookHeadersByBookTitleAndBookAuthorsIn(bookTitle, authors);
    }

}
