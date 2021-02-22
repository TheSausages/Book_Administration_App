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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class LoadDatabase {

    @Bean
    public CommandLineRunner initDatabase(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository, BookService bookService) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

            logger.info("Preloading:" + publisherRepository.save(new Publisher("Publisher1", "Rzeszow")));
            logger.info("Preloading:" + publisherRepository.save(new Publisher("Publisher2", "Krasne")));

            InputStream au1 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Author1.png"));
            InputStream au2 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Author2.png"));

            logger.info("Preloading:" + authorRepository.save(new Author("Kacper", "Ziejlo", LocalDate.of(1999,9,26), au1.readAllBytes())));
            logger.info("Preloading:" + authorRepository.save(new Author("Dominik", "Ziejlo", LocalDate.of(2011,7,2), au2.readAllBytes())));

            InputStream ks1 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Book1.png"));
            InputStream ks2 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Book1.png"));
            InputStream ks3 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Book1.png"));

            Book book1 = new Book("ksiazka1", "opis1", 2005, ks1.readAllBytes(), authorRepository.findById(1l).get(), publisherRepository.findById(1l).get());
            logger.info("Preloading:" + bookRepository.save(book1));

            Book book2 = new Book("ksiazka2", "opis3", 2002, ks2.readAllBytes(), authorRepository.findById(1l).get(), publisherRepository.findById(1l).get());
            logger.info("Preloading:" + bookRepository.save(book2));

            Book book3 = new Book("ksiazka3", "opis3", 2002, ks3.readAllBytes(), authorRepository.findById(1l).get(), publisherRepository.findById(1l).get());
            logger.info("Preloading:" + bookRepository.save(book3));

            InputStream ks4 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Book2.png"));
            InputStream ks5 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Book2.png"));
            InputStream ks6 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/Book2.png"));

            Book book4 = new Book("ksiazka4", "opis2", 2000, ks4.readAllBytes(), authorRepository.findById(2l).get(), publisherRepository.findById(2l).get());
            logger.info("Preloading:" + bookRepository.save(book4));

            Book book5 = new Book("ksiazka5", "opis3", 2002, ks5.readAllBytes(), authorRepository.findById(2l).get(), publisherRepository.findById(2l).get());
            logger.info("Preloading:" + bookRepository.save(book5));

            Book book6 = new Book("ksiazka6", "opis4", 1995, ks6.readAllBytes(), authorRepository.findById(2l).get(), publisherRepository.findById(2l).get());
            logger.info("Preloading:" + bookRepository.save(book6));
        };
    }
}
