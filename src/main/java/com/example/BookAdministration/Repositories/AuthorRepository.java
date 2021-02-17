package com.example.BookAdministration.Repositories;

import com.example.BookAdministration.Entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorRepository extends JpaRepository<Author, Long> {
}
