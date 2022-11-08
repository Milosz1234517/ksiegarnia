package com.example.bookstore.service;

import com.example.bookstore.model.dto.BookReviewsDTO;
import com.example.bookstore.model.dto.WarehouseDTO;
import com.example.bookstore.model.entities.*;
import com.example.bookstore.payload.request.BookCreateRequest;
import com.example.bookstore.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookService {

    BookHeaderRepository bookHeaderRepository;
    PublishingHouseRepository publishingHouseRepository;
    WarehouseRepository warehouseRepository;
    AuthorRepository authorRepository;
    CategoryRepository categoryRepository;
    BookReviewsRepository bookReviewsRepository;
    ModelMapper modelMapper;

    @Autowired
    public void setPublishingHouseRepository(PublishingHouseRepository publishingHouseRepository) {
        this.publishingHouseRepository = publishingHouseRepository;
    }

    @Autowired
    public void setBookReviewsRepository(BookReviewsRepository bookReviewsRepository) {
        this.bookReviewsRepository = bookReviewsRepository;
    }

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

    public static Specification<Warehouse> nameContains(String expression) {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder
                    .like(
                            builder.upper(
                                    root
                                            .join("bookHeader")
                                            .join("bookAuthors")
                                            .get("name")
                            ),
                            contains(expression).toUpperCase()
                    );
        };
    }

    public static Specification<Warehouse> titleContains(String expression) {
        return (root, query, builder) -> builder
                .like(
                        root
                                .join("bookHeader")
                                .get("bookTitle"),
                        contains(expression)
                );
    }

    public static Specification<Warehouse> surnameContains(String expression) {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder
                    .like(
                            builder.upper(
                                    root
                                            .join("bookHeader")
                                            .join("bookAuthors")
                                            .get("surname")
                            ),
                            contains(expression).toUpperCase()
                    );
        };

    }

    public static Specification<Warehouse> availableBooks() {
        return (root, query, builder) -> builder.greaterThan(root.get("quantity"), 0);
    }

    public static Specification<Warehouse> priceLow(Integer price) {
        return (root, query, builder) -> builder.greaterThan(root.get("price"), price);
    }

    public static Specification<Warehouse> priceHigh(Integer price) {
        return (root, query, builder) -> builder.lessThan(root.get("price"), price);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

    public List<WarehouseDTO> searchBooksFilter(
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
                ).stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, WarehouseDTO.class))
                .distinct()
                .toList();
    }

    public List<BookReviewsDTO> getBookReviews(Integer bookHeaderId, Integer page) {
        return bookReviewsRepository
                .findByBookHeader_BookHeaderId(bookHeaderId, PageRequest.of(--page, 20))
                .stream()
                .map(bookReviews -> modelMapper
                        .map(bookReviews, BookReviewsDTO.class))
                .toList();
    }

    public BookHeader addBook(BookCreateRequest bookCreateRequest) {
        throw new RuntimeException();
    }
}
