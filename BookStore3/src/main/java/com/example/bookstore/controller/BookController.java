package com.example.bookstore.controller;

import com.example.bookstore.model.dto.BookHeaderDTO.*;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class BookController {

    private final BookService bookService;

    @GetMapping("/getBooksByTitle")
    public Set<String> searchBooksByTitle(@RequestParam String title, @RequestParam Integer page) {
        return bookService.searchBooksByTitle(title, page);
    }

    @GetMapping("/getBooksFilter")
    public List<BookHeaderDTO> getBooksFilter(
            String name,
            String surname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            String category,
            @RequestParam Boolean available,
            @RequestParam Integer page) {
        return bookService.searchBooksFilter(name, surname, title, priceLow, priceHigh, page, available, category);
    }

    @GetMapping("/getBooksFilterCount")
    public Long getBooksFilterCount(
            String name,
            String surname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            String category,
            @RequestParam Boolean available
    ){
        return bookService.searchBooksFilterCount(name, surname, title, priceLow, priceHigh, available, category);
    }

    @GetMapping("/getBookWithDetails")
    public BookHeaderDetailsDTO getBookWithDetails(@RequestParam Integer bookHeaderId) {
        return bookService.getBookWithDetails(bookHeaderId);
    }

    @GetMapping("/getBooksByCategory")
    public List<BookHeaderDTO> getBooksByCategory(@RequestParam String category, @RequestParam Integer page) {
        return bookService.getBooksByCategory(category, page);
    }

    @PostMapping("/addBook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookHeaderCreateDTO bookHeaderDTO){
        bookService.addBook(bookHeaderDTO);
        return ResponseEntity.ok(new MessageResponse("Book added successfully"));
    }

    @PutMapping("/updateBook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBook(@Valid @RequestBody BookHeaderUpdateDTO bookHeaderDTO){
        bookService.updateBook(bookHeaderDTO);
        return ResponseEntity.ok(new MessageResponse("Book updated successfully"));
    }



}
