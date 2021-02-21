package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        logger.info("Find all Books");

        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        logger.info("Find Book with id:" + id);

        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find Book with id:" + id));
    }

    public Book createBook(Book book) {
        logger.info("Create new Book");

        if (bookRepository.findByTitleAndPublishingYearAndAuthorAndPublisher(book.getTitle(), book.getPublishingYear(), book.getAuthor(), book.getPublisher()).isPresent()) {
            throw new EntityAlreadyExistException("This Book already exists!");
        } else {
            return bookRepository.save(book);
        }
    }

    public HttpStatus deleteBookById(Long id) {
        logger.info("Delete book with id:" + id);

        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);

            return HttpStatus.OK;
        } else {
            throw new EntityNotFoundException("Could not find author with id:" + id);
        }
    }

    public Book updateBook(Book book, Long id) {
        logger.info("Book with id:" + id + " will be updated");

        return bookRepository.findById(id)
                .map(book1 -> {
                    book1.setParamsFromAnotherBook(book);

                    return bookRepository.save(book1);
                })
                .orElseGet(() -> {
                    book.setId(id);

                    return bookRepository.save(book);
                });
    }
}
