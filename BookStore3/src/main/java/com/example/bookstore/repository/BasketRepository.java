package com.example.bookstore.repository;

import com.example.bookstore.model.entities.Basket;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Integer> {

    List<Basket> findByUser_Login(String login, Sort sort);

    Optional<Basket> findByBookHeader_BookHeaderIdAndUser_Login(int bookHeaderId, String login);

}
