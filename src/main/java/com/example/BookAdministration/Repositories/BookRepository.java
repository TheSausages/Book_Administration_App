package com.example.BookAdministration.Repositories;

import com.example.BookAdministration.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Long, Book> {
}
