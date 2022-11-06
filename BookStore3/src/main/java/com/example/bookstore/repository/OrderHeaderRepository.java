package com.example.bookstore.repository;

import com.example.bookstore.model.entities.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Integer> {
}
