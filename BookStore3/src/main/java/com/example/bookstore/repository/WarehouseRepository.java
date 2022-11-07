package com.example.bookstore.repository;

import com.example.bookstore.model.entities.Warehouse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {

    List<Warehouse> findByBookHeader_BookTitleLikeIgnoreCase(
            String bookTitle,
            Pageable pageable
    );

    List<Warehouse> findByQuantityGreaterThan(
            Integer quantity,
            Pageable pageable
    );

    List<Warehouse> findByBookHeader_BookTitleLikeIgnoreCaseAndBookHeader_BookCategories_Description(
            String bookTitle,
            String description,
            Pageable pageable
    );

    List<Warehouse> findAllByBookHeader_BookAuthors_NameLikeAndBookHeader_BookAuthors_SurnameLikeAllIgnoreCase(
            String name,
            String surname,
            Pageable pageable
    );




}

