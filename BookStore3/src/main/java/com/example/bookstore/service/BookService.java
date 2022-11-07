package com.example.bookstore.service;

import com.example.bookstore.model.dto.BookHeaderDTO;
import com.example.bookstore.model.dto.Test;
import com.example.bookstore.model.dto.WarehouseDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Warehouse;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookHeaderRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.repository.WarehouseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookService {

    BookHeaderRepository bookHeaderRepository;

    WarehouseRepository warehouseRepository;
    AuthorRepository authorRepository;

    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

    @Autowired
    public void setWarehouseRepository(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

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

    public List<WarehouseDTO> searchBooksByTitle(String bookTitle, Integer page) {
        return warehouseRepository
                .findByBookHeader_BookTitleLikeIgnoreCase(
                        "%" + bookTitle + "%",
                        PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, WarehouseDTO.class))
                .collect(Collectors.toList());
    }

    public List<WarehouseDTO> searchBooksByTitleCategory(String title, String category, Integer page) {
        return warehouseRepository
                .findByBookHeader_BookTitleLikeIgnoreCaseAndBookHeader_BookCategories_Description(
                        "%" + title + "%",
                        category,
                        PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, WarehouseDTO.class))
                .toList();
    }

    public List<WarehouseDTO> searchBooksByTitleCategoryPrice(
            String title, String category, String priceLow, String priceHigh, Integer page) {
        throw new RuntimeException();
    }

    public List<WarehouseDTO> searchBooksByCategoryPrice(
            String title, String priceLow, String priceHigh, Integer page) {
        throw new RuntimeException();
    }

    public List<WarehouseDTO> searchBooksByTitlePrice(
            String title, String priceLow, String priceHigh, Integer page) {
        throw new RuntimeException();
    }

    public static Specification<Warehouse> nameContains(String expression) {
        return (root, query, builder) -> builder
                .like(
                        builder.upper(
                                root
                                        .join("bookHeader")
                                        .join("bookAuthors")
                                        .get("name")
                        ),
                        contains(expression).toUpperCase()
                );
    }

    public static Specification<Warehouse> titleContains(String expression) {
        return (root, query, builder) -> builder
                .like(
                        builder.upper(
                                root
                                        .join("bookHeader")
                                        .get("bookTitle")
                        ),
                        contains(expression).toUpperCase()
                );
    }

    public static Specification<Warehouse> surnameContains(String expression) {
        return (root, query, builder) -> builder
                .like(
                        builder.upper(
                                root
                                        .join("bookHeader")
                                        .join("bookAuthors")
                                        .get("surname")
                        ),
                        contains(expression).toUpperCase()
                );
    }

    public static Specification<Warehouse> availableBooks() {
        return (root, query, builder) -> builder.greaterThan(root.get("quantity"), 0);
    }

    public static Specification<Warehouse> priceLow(Integer price) {
        return (root, query, builder) -> builder.lessThan(root.get("price"), price);
    }

    public static Specification<Warehouse> priceHigh(Integer price) {
        return (root, query, builder) -> builder.greaterThan(root.get("price"), price);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

    public List<WarehouseDTO> searchBooksByAuthor(
            String authorName,
            String authorSurname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            Integer page,
            Boolean available) {
        return warehouseRepository
                .findAll(
                        Specification
                                .where(authorName == null ? null : nameContains(authorName))
                                .and(authorSurname == null ? null : surnameContains(authorSurname))
                                .and(title == null ? null : titleContains(title))
                                .and(priceLow == null ? null : priceLow(priceLow))
                                .and(priceHigh == null ? null : priceHigh(priceHigh))
                                .and(available ? availableBooks() : null),
                        PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, WarehouseDTO.class))
                .toList();
    }

    public List<WarehouseDTO> getAllAvailableBooks(Integer page) {
        return warehouseRepository
                .findByQuantityGreaterThan(
                        0,
                        PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, WarehouseDTO.class))
                .toList();
    }


}
