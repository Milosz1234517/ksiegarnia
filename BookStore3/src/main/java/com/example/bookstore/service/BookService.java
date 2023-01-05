package com.example.bookstore.service;

import com.example.bookstore.exceptions.BadRequestException;
import com.example.bookstore.model.dto.bookDTO.*;
import com.example.bookstore.model.entities.Author;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.model.entities.PublishingHouse;
import com.example.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.text.MessageFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookHeaderRepository bookHeaderRepository;
    private final PublishingHouseRepository publishingHouseRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public Long searchBooksFilterCount(
            String authorName,
            String authorSurname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            Boolean available,
            String category) {

        return bookHeaderRepository
                .count(
                        getBookHeaderSpecification(authorName, authorSurname, title, priceLow, priceHigh, available, category)
                );
    }

    public Set<String> searchBooksByTitle(
            String title,
            Integer page
    ) {

        List<BookHeaderDTO> bookHeaders =bookHeaderRepository
                .findAll(
                        Specification
                                .where(title == null ? null : titleContains(title)),
                        PageRequest.of(--page, 20, Sort.by(Sort.Direction.ASC, "bookHeaderId"))
                ).stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, BookHeaderDTO.class))
                .distinct()
                .toList();
        Set<String> uniqueTitles = new HashSet<>();
        bookHeaders.forEach(bookHeaderDTO -> uniqueTitles.add(bookHeaderDTO.getBookTitle().toLowerCase()));

        return uniqueTitles;
    }

    public List<BookHeaderDTO> searchBooksFilter(
            String authorName,
            String authorSurname,
            String title,
            Integer priceLow,
            Integer priceHigh,
            Integer page,
            Boolean available,
            String category) {

        return bookHeaderRepository
                .findAll(
                        getBookHeaderSpecification(authorName, authorSurname, title, priceLow, priceHigh, available, category),
                        PageRequest.of(--page, 20, Sort.by(Sort.Direction.ASC, "bookHeaderId"))
                ).stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, BookHeaderDTO.class))
                .distinct()
                .toList();
    }

    private static Specification<BookHeader> getBookHeaderSpecification(String authorName, String authorSurname,
                                                                        String title, Integer priceLow, Integer priceHigh,
                                                                        Boolean available, String category) {
        return Specification
                .where(authorName == null ? null : nameContains(authorName))
                .and(authorSurname == null ? null : surnameContains(authorSurname))
                .and(title == null ? null : titleContains(title))
                .and(priceLow == null ? null : priceLow(priceLow))
                .and(priceHigh == null ? null : priceHigh(priceHigh))
                .and(category == null ? null : category(category))
                .and(available ? availableBooks() : null);
    }

    public BookHeader getBookWithDetails(Integer bookHeaderId) {
        return bookHeaderRepository
                .findByBookHeaderId(bookHeaderId).orElseThrow(() -> new BadRequestException("Book Header not exist"));
    }

    public void addBook(BookHeaderCreateDTO bookHeaderDTO) {
        BookHeader bookHeader = modelMapper.map(bookHeaderDTO, BookHeader.class);

        setCategories(bookHeader);
        setAuthors(bookHeader);
        setPublishingHouse(bookHeader);

        bookHeaderRepository.save(bookHeader);
    }

    public void updateBook(BookHeaderUpdateDTO bookHeaderDTO) {
        BookHeader bookHeader = modelMapper.map(bookHeaderDTO, BookHeader.class);

        setCategories(bookHeader);
        setAuthors(bookHeader);
        setPublishingHouse(bookHeader);

        bookHeaderRepository.save(bookHeader);
    }

    public List<BookHeaderDTO> getBooksByCategory(String category, Integer page) {
        return bookHeaderRepository
                .findDistinctByBookCategories_DescriptionLikeIgnoreCase(contains(category), PageRequest.of(--page, 20))
                .stream()
                .map(bookHeader -> modelMapper.map(bookHeader, BookHeaderDTO.class))
                .toList();
    }

    private static Specification<BookHeader> nameContains(String expression) {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder
                    .like(
                            builder.upper(
                                    root
                                            .join("bookAuthors")
                                            .get("name")
                            ),
                            contains(expression).toUpperCase()
                    );
        };
    }

    private static Specification<BookHeader> titleContains(String expression) {
        return (root, query, builder) -> builder
                .like(
                        builder.upper(
                                root.get("bookTitle")
                        ),
                        contains(expression).toUpperCase()
                );
    }

    private static Specification<BookHeader> category(String expression) {
        return (root, query, builder) -> builder
                .like(
                        builder.upper(
                                root.join("bookCategories")
                                        .get("description")
                        ),
                        contains(expression).toUpperCase()
                );
    }

    private static Specification<BookHeader> surnameContains(String expression) {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder
                    .like(
                            builder.upper(
                                    root
                                            .join("bookAuthors")
                                            .get("surname")
                            ),
                            contains(expression).toUpperCase()
                    );
        };

    }

    private static Specification<BookHeader> availableBooks() {
        return (root, query, builder) -> builder.greaterThan(root.get("quantity"), 0);
    }

    private static Specification<BookHeader> priceLow(Integer price) {
        return (root, query, builder) -> builder.greaterThan(root.get("price"), price);
    }

    private static Specification<BookHeader> priceHigh(Integer price) {
        return (root, query, builder) -> builder.lessThan(root.get("price"), price);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

    private void setPublishingHouse(BookHeader bookHeaderDTO) {
        Optional<PublishingHouse> publishingHouse = publishingHouseRepository
                .findByNameIgnoreCase(bookHeaderDTO.getPublishingHouse().getName());

        if (publishingHouse.isEmpty())
            publishingHouseRepository.save(bookHeaderDTO.getPublishingHouse());
        else
            bookHeaderDTO.getPublishingHouse().setPublishingHouseId(publishingHouse.get().getPublishingHouseId());
    }

    private void setAuthors(BookHeader bookHeaderDTO) {
        bookHeaderDTO
                .getBookAuthors()
                .forEach(author -> {
                    Optional<Author> aut = authorRepository
                            .findByNameIgnoreCaseAndSurnameIgnoreCase(author.getName(), author.getSurname());
                    if (aut.isEmpty())
                        authorRepository.save(author);
                    else
                        author.setAuthorId(aut.get().getAuthorId());
                });
    }

    private void setCategories(BookHeader bookHeaderDTO) {
        bookHeaderDTO
                .getBookCategories()
                .forEach(category -> {
                    Optional<Category> cat = categoryRepository.findByDescriptionIgnoreCase(category.getDescription());
                    if (cat.isEmpty())
                        cat = Optional.of(categoryRepository.save(modelMapper.map(category, Category.class)));
                    category.setCategoryId(cat.get().getCategoryId());
                });
    }
}
