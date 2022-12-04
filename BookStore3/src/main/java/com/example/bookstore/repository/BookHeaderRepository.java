package com.example.bookstore.repository;

import com.example.bookstore.model.entities.BookHeader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookHeaderRepository extends JpaRepository<BookHeader, Long>, JpaSpecificationExecutor<BookHeader> {

    List<BookHeader> findByBookTitleLikeIgnoreCase(
            String bookTitle,
            Pageable pageable
    );

    Optional<BookHeader> findByBookHeaderId(Integer bookHeaderId);

    List<BookHeader> findDistinctByBookCategories_DescriptionLikeIgnoreCase(String category, Pageable pageable);

    List<BookHeader> findDistinctByBookTitle(String bookTitle, Pageable pageable);







}

