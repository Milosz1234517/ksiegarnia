package com.example.bookstore.controller;

import com.example.bookstore.model.dto.BookHeaderDTO;
import com.example.bookstore.model.dto.WarehouseDTO;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    public List<WarehouseDTO> searchBooksByTitle(@RequestParam String title, @RequestParam Integer page) {
        return bookService.searchBooksByTitle(title, page);
    }

    @GetMapping("/getBooksByAuthor")
    public List<WarehouseDTO> searchBooksByAuthor(
            String name,
            String surname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            Boolean available,
            @RequestParam Integer page) {
        return bookService.searchBooksByAuthor(name, surname, title, priceLow, priceHigh, page, available);
    }

    @GetMapping("/getBooksByTitleCategory")
    public List<WarehouseDTO> searchBooksByTitleCategory(String title, String category, Integer page) {
        return bookService.searchBooksByTitleCategory(title, category, page);
    }

    @GetMapping("/getHomePageBooks")
    public List<WarehouseDTO> getHomepageBooks(@RequestParam Integer page) {
        return bookService.getAllAvailableBooks(page);
    }
}
