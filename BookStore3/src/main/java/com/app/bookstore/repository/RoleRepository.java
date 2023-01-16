package com.app.bookstore.repository;

import java.util.Optional;

import com.app.bookstore.model.entities.role.ERole;
import com.app.bookstore.model.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
