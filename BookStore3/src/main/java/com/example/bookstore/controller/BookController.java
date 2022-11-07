package com.example.bookstore.controller;

import com.example.bookstore.model.dto.BookHeaderDTO;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bookstore")
public class BookController {

    BookService bookService;

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/getBooksByTitle")
    public List<BookHeaderDTO> searchBooksByTitle(@RequestParam String title, @RequestParam Integer page) {
        return bookService.searchBooksByTitle(title, page);
    }

    @GetMapping("/getBooksByTitleCategory")
    public List<BookHeaderDTO> searchBooksByTitleCategory(@RequestParam String title, @RequestParam String category, @RequestParam Integer page) {
        return bookService.searchBooksByTitleCategory(title, category, page);
    }

    @GetMapping("/getHomePageBooks")
    public List<BookHeaderDTO> getHomepageBooks(@RequestParam Integer page) {
        return bookService.getAllAvailableBooks(page);
    }
}
