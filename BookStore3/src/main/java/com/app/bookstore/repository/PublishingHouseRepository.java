package com.app.bookstore.repository;

import com.app.bookstore.model.entities.PublishingHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublishingHouseRepository extends JpaRepository<PublishingHouse, Integer> {
    Optional<PublishingHouse> findByNameIgnoreCase(String name);
}
