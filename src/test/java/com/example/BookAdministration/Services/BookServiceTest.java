package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityHasChildrenException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_NoErrors_NormalBehavior() {
        //given
        Book book = new Book();
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));

        //when
        List<Book> books = bookService.getAllBooks();

        //then
        assertEquals(Arrays.asList(book), books);
    }

    @Test
    void getBookById_NoSuchBook_ThrowException() {
        //given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });

        //then
        assertEquals("Could not find Book with id:1", exception.getMessage());
    }

    @Test
    void getBookById_NoErrors_normalBehavior() {
        //given
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        //when
        Book bookReturned = bookService.getBookById(1L);

        //then
        assertEquals(bookReturned, book);
    }

    @Test
    void get3BooksByAuthorId_NoSuchAuthor_ThrowError() {
        //given
        when(bookRepository.findFirst3BooksByAuthorId(1L)).thenReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.get3BooksByAuthorId(1L);
        });

        //then
        assertEquals("No books of given Author in database", exception.getMessage());
    }

    @Test
    void get3BooksByAuthorId_NoErrors_NormalBehavior() {
        //given
        Book book = new Book();
        when(bookRepository.findFirst3BooksByAuthorId(1L)).thenReturn(Optional.of(Arrays.asList(book)));

        //when
        List<Book> books = bookService.get3BooksByAuthorId(1L);

        //then
        assertEquals(books, Arrays.asList(book));
    }

    @Test
    void findFirst5BooksByAuthorId_NoSuchAuthor_ThrowError() {
        //given
        when(bookRepository.findFirst5BooksByAuthorId(1L)).thenReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.findFirst5BooksByAuthorId(1L);
        });

        //then
        assertEquals("No books of given Author in database", exception.getMessage());
    }

    @Test
    void findFirst5BooksByAuthorId_NoErrors_NormalBehavior() {
        //given
        Book book = new Book();
        when(bookRepository.findFirst5BooksByAuthorId(1L)).thenReturn(Optional.of(Arrays.asList(book)));

        //when
        List<Book> books = bookService.findFirst5BooksByAuthorId(1L);

        //then
        assertEquals(books, Arrays.asList(book));
    }

    @Test
    void createBook_BookAlreadyExists_ThrowError() {
        //given
        Author author = new Author();
        Publisher publisher = new Publisher();
        Book book = setTypicalParams(new Book(), author, publisher);
        when(bookRepository.existsByTitleAndSubTitleAndPublishingYearAndAuthorAndPublisher("Title",
                "Subtitle", 2000, author, publisher)).thenReturn(true);

        //when
        RuntimeException exception = assertThrows(EntityAlreadyExistException.class, () -> {
            bookService.createBook(book);
        });

        //then
        assertEquals("This Book already exists!", exception.getMessage());
    }

    @Test
    void createBook_NoErrors_NormalBehavior() {
        //given
        Author author = new Author();
        Publisher publisher = new Publisher();
        Book book = setTypicalParams(new Book(), author, publisher);
        when(bookRepository.existsByTitleAndSubTitleAndPublishingYearAndAuthorAndPublisher("Title",
                "Subtitle", 2000, author, publisher)).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(book);

        //when
        Book bookReturned = bookService.createBook(book);

        //then
        assertEquals(bookReturned, book);
    }

    @Test
    void deleteBookById_NoSuchBook_ThrowException() {
        //given
        when(bookRepository.existsById(1L)).thenReturn(false);

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteBookById(1L);
        });

        //then
        assertEquals("Could not find author with id:1", exception.getMessage());
    }

    @Test
    void deleteBookById_NoErrors_NormalBehavior() {
        //given
        when(bookRepository.existsById(1L)).thenReturn(true);

        //when
        HttpStatus status = bookService.deleteBookById(1L);

        //then
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void updateBook_BookAlreadyExist_ThrowError() {
        Publisher publisher = new Publisher();
        Author author = new Author();
        Book book = setTypicalParams(new Book(), author, publisher);
        book.setId(2L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByTitleAndSubTitleAndPublishingYearAndAuthorAndPublisher(book.getTitle(), book.getSubTitle(), book.getPublishingYear(),
                book.getAuthor(), book.getPublisher())).thenReturn(Optional.of(setTypicalParams(new Book(), author, publisher)));

        //when
        RuntimeException exception = assertThrows(EntityAlreadyExistException.class, () -> {
            bookService.updateBook(book, 1L);
        });

        //then
        assertEquals("This Book already exists!", exception.getMessage());
    }

    @Test
    void updateBook_BookAlreadyExist_UpdateInfo() {
        Publisher publisher = new Publisher();
        Author author = new Author();
        Book book = setTypicalParams(new Book(), author, publisher);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByTitleAndSubTitleAndPublishingYearAndAuthorAndPublisher(book.getTitle(), book.getSubTitle(), book.getPublishingYear(),
                book.getAuthor(), book.getPublisher())).thenReturn(Optional.of(setTypicalParams(new Book(), author, publisher)));
        when(bookRepository.save(book)).thenReturn(book);

        //when
        Book bookReturned = bookService.updateBook(book, 1L);

        //then
        assertEquals(book, bookReturned);
    }

    @Test
    void updateBook_BookAlreadyExist_createAuthor() {
        Publisher publisher = new Publisher();
        Author author = new Author();
        Book book = setTypicalParams(new Book(), author, publisher);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        //when
        Book bookReturned = bookService.updateBook(book, 1L);

        //then
        assertEquals(book, bookReturned);
    }

    @Test
    void checkIfAnyBooksByPublisherId_PublisherHasChildren_ThrowException() {
        //when
        when(bookRepository.existsByPublisherId(1L)).thenReturn(true);

        //when
        RuntimeException exception = assertThrows(EntityHasChildrenException.class, () -> {
            bookService.checkIfAnyBooksByPublisherId(1L);
        });

        //then
        assertEquals(exception.getMessage(), "There are Books in the database that state this Publisher! Delete them and attempt again");
    }

    @Test
    void checkIfAnyBooksByPublisherId_NoErrors_NormalBehavior() {
        //when
        when(bookRepository.existsByPublisherId(1L)).thenReturn(false);

        //when
        bookService.checkIfAnyBooksByPublisherId(1L);

        //then
    }

    @Test
    void checkIfAnyBooksByAuthorId_AuthorHasChildren_ThrowException() {
        //when
        when(bookRepository.existsByAuthorId(1L)).thenReturn(true);

        //when
        RuntimeException exception = assertThrows(EntityHasChildrenException.class, () -> {
            bookService.checkIfAnyBooksByAuthorId(1L);
        });

        //then
        assertEquals(exception.getMessage(), "There are Books in the database that state this Author! Delete them and attempt again");
    }

    @Test
    void checkIfAnyBooksByAuthorId_NoErrors_NormalBehavior() {
        //when
        when(bookRepository.existsByAuthorId(1L)).thenReturn(false);

        //when
        bookService.checkIfAnyBooksByAuthorId(1L);

        //then
    }


    private static Book setTypicalParams(Book book, Author author, Publisher publisher) {
        book.setId(1L);
        book.setTitle("Title");
        book.setSubTitle("Subtitle");
        book.setDescription("Description");
        book.setPublishingYear(2000);
        book.setAuthor(author);
        book.setPublisher(publisher);

        return book;
    }
}