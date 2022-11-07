package com.example.bookstore.repository;

import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookHeaderRepository extends JpaRepository<BookHeader, Long> {

}

