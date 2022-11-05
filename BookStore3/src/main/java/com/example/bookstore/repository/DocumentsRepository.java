package com.example.bookstore.repository;

import com.example.bookstore.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Integer> {
}
