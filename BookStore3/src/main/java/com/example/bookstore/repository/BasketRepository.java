package com.example.bookstore.repository;

import com.example.bookstore.model.entities.Basket;
import com.example.bookstore.model.entities.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Integer> {

    List<Basket> findByUser_Login(String login, Sort sort);

    @Transactional
    void deleteByUser(Users user);

    Optional<Basket> findByBookHeader_BookHeaderIdAndUser_Login(int bookHeaderId, String login);

}
