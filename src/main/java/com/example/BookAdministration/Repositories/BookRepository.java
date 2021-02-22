package com.example.BookAdministration.Repositories;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleAndPublishingYearAndAuthorAndPublisher(String title, Integer publishingYear, Author author, Publisher publisher);

    Optional<List<Book>> findFirst3BooksByAuthorId(Long id);
}
