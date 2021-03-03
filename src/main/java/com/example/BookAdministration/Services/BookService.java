package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityHasChildrenException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Repositories.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Book> get3BooksByAuthorId(Long id) {
        logger.info("Find first 3 books by Author with id:" + id);

        return bookRepository.findFirst3BooksByAuthorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No books of given Author in database"));
    }

    public List<Book> findFirst5BooksByAuthorId(Long id) {
        logger.info("Get 5 random books by Author with id:" + id);

        return bookRepository.findFirst5BooksByAuthorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No books of given Author in database"));
    }

    public Book createBook(Book book) {
        logger.info("Create new Book");

        if (bookRepository.existsBookByTitleAndSubTitleAndPublishingYearAndAuthorAndPublisher(book.getTitle(), book.getSubTitle(), book.getPublishingYear(), book.getAuthor(), book.getPublisher())) {
            throw new EntityAlreadyExistException("This Book already exists!");
        }

        return bookRepository.save(book);
    }

    public HttpStatus deleteBookById(Long id) {
        logger.info("Delete book with id:" + id);

        if (bookRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Could not find author with id:" + id);
        }

        bookRepository.deleteById(id);

        return HttpStatus.OK;
    }

    public Book updateBook(Book book, Long id) {
        logger.info("Book with id:" + id + " will be updated");

        return bookRepository.findById(id)
                .map(book1 -> {
                    Optional<Book> possibleDub = bookRepository.findByTitleAndSubTitleAndPublishingYearAndAuthorAndPublisher(book.getTitle(), book.getSubTitle(), book.getPublishingYear(), book.getAuthor(), book.getPublisher());

                    if (possibleDub.isPresent() && (possibleDub.get().getId() != book.getId())) {
                        throw new EntityAlreadyExistException("This Book already exists!");
                    }

                    book1.setParamsFromAnotherBook(book);

                    return bookRepository.save(book1);
                })
                .orElseGet(() -> {
                    book.setId(id);

                    return bookRepository.save(book);
                });
    }

    public void checkIfAnyBooksByPublisherId(Long id) {
        logger.info("Checked if any there is any Book by publisher with id:" + id);

        if (bookRepository.existsByPublisherId(id)) {
            throw new EntityHasChildrenException("There are Books in the database that state this Publisher! Delete them and attempt again");
        }
    }

    public void checkIfAnyBooksByAuthorId(Long id) {
        logger.info("Checked if any there is any Book by publisher with id:" + id);

        if (bookRepository.existsByAuthorId(id)) {
            throw new EntityHasChildrenException("There are Books in the database that state this Author! Delete them and attempt again");
        }
    }
}
