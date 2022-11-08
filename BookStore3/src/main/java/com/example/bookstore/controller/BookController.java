package com.example.bookstore.controller;

import com.example.bookstore.model.dto.BookHeaderCreateDTO;
import com.example.bookstore.model.dto.BookHeaderDTO;
import com.example.bookstore.model.dto.BookReviewsDTO;
import com.example.bookstore.model.dto.WarehouseDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.BookReviews;
import com.example.bookstore.payload.request.BookCreateRequest;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getBooksFilter")
    public List<WarehouseDTO> searchBooksByAuthor(
            String name,
            String surname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            @RequestParam Boolean available,
            @RequestParam Integer page) {
        return bookService.searchBooksFilter(name, surname, title, priceLow, priceHigh, page, available);
    }

    @GetMapping("/getBookReviews")
    public List<BookReviewsDTO> getBookReviews(@RequestParam Integer bookHeaderId, @RequestParam Integer page) {
        return bookService.getBookReviews(bookHeaderId, page);
    }

    @PostMapping("/addBook")
    public BookHeader addBook(@RequestBody BookCreateRequest bookCreateRequest){
        return bookService.addBook(bookCreateRequest);
    }

}
