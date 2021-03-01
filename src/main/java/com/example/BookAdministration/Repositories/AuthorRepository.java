package com.example.BookAdministration.Repositories;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.PrimaryGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    Optional<Author> findByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);
}
