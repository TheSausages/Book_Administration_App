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

        if (authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName()).isPresent()) {
            throw new EntityAlreadyExistException("This Author already exists!");
        } else {
            return authorRepository.save(author);
        }
    }

    public HttpStatus deleteAuthorById(Long id) {
        logger.info("Delete author with id:" + id);

        if (authorRepository.findById(id).isPresent()) {
            authorRepository.deleteById(id);

            return HttpStatus.OK;
        } else {
            throw new EntityNotFoundException("Could not find author with id:" + id);
        }
    }

    public Author updateAuthor(Author author, Long id) {
        logger.info("Author with id:" + id + " will be updated");

        return authorRepository.findById(id)
                .map(author1 -> {
                    author1.setFirstName(author.getFirstName());
                    author1.setLastName(author.getLastName());
                    author1.setPortrait(author.getPortrait());

                    return authorRepository.save(author1);
                })
                .orElseGet(() -> {
                    author.setId(id);

                    return authorRepository.save(author);
                });
    }
}
