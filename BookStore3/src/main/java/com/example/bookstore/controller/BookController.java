package com.example.bookstore.controller;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDTO;
import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDetailsDTO;
import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderNoIdDTO;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<BookHeaderDTO> searchBooksByTitle(@RequestParam String title, @RequestParam Integer page) {
        return bookService.searchBooksByTitle(title, page);
    }

    @GetMapping("/getBooksFilter")
    public List<BookHeaderDTO> searchBooksByAuthor(
            String name,
            String surname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            @RequestParam Boolean available,
            @RequestParam Integer page) {
        return bookService.searchBooksFilter(name, surname, title, priceLow, priceHigh, page, available);
    }

    @GetMapping("/getBookWithDetails")
    public List<BookHeaderDetailsDTO> getBookReviews(@RequestParam Integer bookHeaderId) {
        return bookService.getBookWithDetails(bookHeaderId);
    }

    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@RequestBody BookHeaderNoIdDTO bookHeaderDTO){
        bookService.addBook(bookHeaderDTO);
        return ResponseEntity.ok(new MessageResponse("Book added successfully"));
    }

}
