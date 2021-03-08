package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.PrimaryGenre;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Repositories.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void getAllAuthors_NoErrors_NormalBehavior() {
        //given
        Author author = new Author();
        when(authorRepository.findAll()).thenReturn(Arrays.asList(author));

        //when
        List<Author> result = authorService.getAllAuthors();

        //then
        assertEquals(result, Arrays.asList(author));
    }

    @Test
    void getAuthorById_NoSuchAuthor_throwException() {
        //given
        when(authorRepository.findById(1L)).thenThrow(new EntityNotFoundException("No Such Author"));

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            authorService.getAuthorById(1L);
        });

        //then
        assertEquals("No Such Author", exception.getMessage());
    }

    @Test
    void getAuthorById_NoErrors_NormalBehavior() {
        //given
        Author author = new Author();
        when(authorRepository.findById(1L)).thenReturn(java.util.Optional.of(author));

        //when
        Author authorReturned = authorService.getAuthorById(1L);

        //then
        assertEquals(authorReturned, author);
    }

    @Test
    void createAuthor_AuthorAlreadyExist_throwException() {
        //given
        Author author = setTypicalParams(new Author());
        when(authorRepository.existsByFirstNameAndLastNameAndDateOfBirth("firstName",
                "lastName", LocalDate.of(1999, 9, 26))).thenReturn(true);

        //when
        RuntimeException exception = assertThrows(EntityAlreadyExistException.class, () -> {
            authorService.createAuthor(author);
        });

        //then
        assertEquals("This Author already exists!", exception.getMessage());
    }

    @Test
    void createAuthor_NoErrors_NormalBehavior() {
        //given
        Author author = setTypicalParams(new Author());
        when(authorRepository.existsByFirstNameAndLastNameAndDateOfBirth("firstName",
                "lastName", LocalDate.of(1999, 9, 26))).thenReturn(false);
        when(authorRepository.save(author)).thenReturn(author);

        //when
        Author authorReturned = authorService.createAuthor(author);

        //then
        assertEquals(authorReturned, author);
    }

    @Test
    void deleteAuthorById_NoSuchAuthor_ThrowError() {
        //given
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            authorService.deleteAuthorById(1L);
        });

        //then
        assertEquals("Could not find author with id:1", exception.getMessage());
    }

    @Test
    void deleteAuthorById_NoErrors_NormalBehavior() {
        //given
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));

        //when
        HttpStatus status = authorService.deleteAuthorById(1L);

        //then
        assertEquals(status, HttpStatus.OK);
    }

    @Test
    void updateAuthor_AuthorAlreadyExist_ThrowError() {
        //given
        Author author = setTypicalParams(new Author());
        author.setId(2);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.findByFirstNameAndLastNameAndDateOfBirth("firstName", "lastName",
                LocalDate.of(1999, 9, 26))).thenReturn(Optional.of(setTypicalParams(new Author())));

        //when
        RuntimeException exception = assertThrows(EntityAlreadyExistException.class, () -> {
            authorService.updateAuthor(author, 1L);
        });

        //then
        assertEquals("Author with those information already Exists!", exception.getMessage());
    }

    @Test
    void updateAuthor_AuthorAlreadyExist_UpdateInfo() {
        //given
        Author author = setTypicalParams(new Author());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.findByFirstNameAndLastNameAndDateOfBirth("firstName", "lastName",
                LocalDate.of(1999, 9, 26))).thenReturn(Optional.of(setTypicalParams(new Author())));
        when(authorRepository.save(author)).thenReturn(author);

        //when
        Author authorReturned = authorService.updateAuthor(author, 1L);

        //then
        assertEquals(authorReturned, author);
    }

    @Test
    void updateAuthor_AuthorDoesNotExist_CreateAuthor() {
        //given
        Author author = setTypicalParams(new Author());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        when(authorRepository.save(author)).thenReturn(author);

        //when
        Author authorReturned = authorService.updateAuthor(author, 1L);

        //then
        assertEquals(authorReturned, author);
    }

    private static Author setTypicalParams(Author author) {
        author.setId(1);
        author.setFirstName("firstName");
        author.setLastName("lastName");
        author.setDateOfBirth(LocalDate.of(1999,9,26));
        author.setPrimaryGenre(PrimaryGenre.Fantasy);

        return author;
    }
}