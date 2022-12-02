package com.example.bookstore.controller;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDTO;
import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDetailsDTO;
import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDetailsIdIgnoreDTO;
import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderGetDetailsDTO;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class BookController {

    private final BookService bookService;

    @GetMapping("/getBooksByTitle")
    public List<BookHeaderDTO> searchBooksByTitle(@RequestParam String title, @RequestParam Integer page) {
        return bookService.searchBooksByTitle(title, page);
    }

    @GetMapping("/getBooksFilter")
    public List<BookHeaderDTO> getBooksFilter(
            String name,
            String surname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            @RequestParam Boolean available,
            @RequestParam Integer page) {
        return bookService.searchBooksFilter(name, surname, title, priceLow, priceHigh, page, available);
    }

    @GetMapping("/getBooksFilterCount")
    public Long getBooksFilterCount(
            String name,
            String surname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            @RequestParam Boolean available
    ){
        return bookService.searchBooksFilterCount(name, surname, title, priceLow, priceHigh, available);
    }

    @GetMapping("/getBookWithDetails")
    public BookHeaderGetDetailsDTO getBookWithDetails(@RequestParam Integer bookHeaderId) {
        return bookService.getBookWithDetails(bookHeaderId);
    }

    @GetMapping("/getBooksByCategory")
    public List<BookHeaderDTO> getBooksByCategory(@RequestParam String category, @RequestParam Integer page) {
        return bookService.getBooksByCategory(category, page);
    }

    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookHeaderDetailsIdIgnoreDTO bookHeaderDTO){
        bookService.addBook(bookHeaderDTO);
        return ResponseEntity.ok(new MessageResponse("Book added successfully"));
    }

    @PutMapping("/updateBook")
    public ResponseEntity<?> updateBook(@Valid @RequestBody BookHeaderDetailsDTO bookHeaderDTO){
        bookService.updateBook(bookHeaderDTO);
        return ResponseEntity.ok(new MessageResponse("Book updated successfully"));
    }



}
