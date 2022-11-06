package com.example.bookstore.repository;

import java.util.Optional;

import com.example.bookstore.model.entities.role.ERole;
import com.example.bookstore.model.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
