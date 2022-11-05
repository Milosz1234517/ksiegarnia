package com.example.bookstore.repository;

import com.example.bookstore.model.BookHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookHeaderRepository extends JpaRepository<BookHeader, Long> {

    List<BookHeader> findBookHeadersByBookTitle(String bookTitle);

    

}
