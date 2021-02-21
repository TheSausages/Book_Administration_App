package com.example.BookAdministration;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Repositories.AuthorRepository;
import com.example.BookAdministration.Repositories.BookRepository;
import com.example.BookAdministration.Repositories.PublisherRepository;
import com.example.BookAdministration.Services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class LoadDatabase {

    @Bean
    public CommandLineRunner initDatabase(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository, BookService bookService) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

            logger.info("Preloading:" + publisherRepository.save(new Publisher("Publisher1", "Rzeszow")));
            logger.info("Preloading:" + publisherRepository.save(new Publisher("Publisher2", "Krasne")));

            logger.info("Preloading:" + authorRepository.save(new Author("Kacper", "Ziejlo")));
            logger.info("Preloading:" + authorRepository.save(new Author("Dominik", "Ziejlo")));

            InputStream ks1 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Book1.png"));
            InputStream ks2 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Book2.png"));

            Book book1 = new Book("ksiazka1", "opis1", 2005, ks1.readAllBytes(), authorRepository.findById(1l).get(), publisherRepository.findById(1l).get());
            logger.info("Preloading:" + bookRepository.save(book1));

            Book book2 = new Book("ksiazka2", "opis2", 2000, ks2.readAllBytes(), authorRepository.findById(2l).get(), publisherRepository.findById(2l).get());
            logger.info("Preloading:" + bookRepository.save(book2));
        };
    }
}
