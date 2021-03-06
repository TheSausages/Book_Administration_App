package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Repositories.AuthorRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        logger.info("Find all Authors");

        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        logger.info("Find Author with id:" + id);

        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find author with id:" + id));
    }

    public Author createAuthor(Author author) {
        logger.info("Create new Author");

        if (authorRepository.existsByFirstNameAndLastNameAndDateOfBirth(author.getFirstName(), author.getLastName(), author.getDateOfBirth())) {
            throw new EntityAlreadyExistException("This Author already exists!");
        }

        return authorRepository.save(author);
    }

    public HttpStatus deleteAuthorById(Long id) {
        logger.info("Delete author with id:" + id);

        if (authorRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Could not find author with id:" + id);
        }

        authorRepository.deleteById(id);

        return HttpStatus.OK;
    }

    public Author updateAuthor(Author author, Long id) {
        logger.info("Author with id:" + id + " will be updated");

        return authorRepository.findById(id)
                .map(author1 -> {
                    Optional<Author> possibleDup = authorRepository.findByFirstNameAndLastNameAndDateOfBirth(author.getFirstName()
                            , author.getLastName(), author.getDateOfBirth());

                    if (possibleDup.isPresent() && (possibleDup.get().getId() != author.getId())) {
                        throw new EntityAlreadyExistException("Author with those information already Exists!");
                    }

                    author1.setParamsFromAnotherAuthor(author);


                    return authorRepository.save(author1);
                })
                .orElseGet(() -> {
                    author.setId(id);

                    return authorRepository.save(author);
                });
    }
}
