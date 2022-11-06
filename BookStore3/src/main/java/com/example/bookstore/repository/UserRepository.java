package com.example.bookstore.repository;

import com.example.bookstore.model.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByLogin(String login);

    Boolean existsByLogin(String login);
}
