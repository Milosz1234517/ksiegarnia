package com.example.bookstore.repository;

import com.example.bookstore.model.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameIgnoreCaseAndSurnameIgnoreCase(String name, String surname);

}
