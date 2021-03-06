package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Services.AuthorService;
import com.example.BookAdministration.Services.BookService;
import com.example.BookAdministration.Services.MyUserDetailsService;
import com.example.BookAdministration.Services.PublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
@WithMockUser
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private PublisherService publisherService;

    @MockBean
    private MyUserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void viewBookCatalog_NoErrors_NormalBehavior() throws Exception {
        Book book = createBook();

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book));

        this.mockMvc
                .perform(get("/books/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookCatalog"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void viewBook_NoErrors_NormalBehavior() throws Exception {
        Book book = createBook();
        when(bookService.getBookById(1l)).thenReturn(book);

        this.mockMvc
                .perform(get("/books/info/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookInfo"))
                .andExpect(model().attribute("book", book));
    }

    @Test
    void newBook_NoErrors_NormalBehavior() throws Exception {
        Book book = setTypicalParams(createBook());

        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(book.getAuthor()));
        when(publisherService.getAllPublishers()).thenReturn(Arrays.asList(book.getPublisher()));

        this.mockMvc
                .perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookForm"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", Arrays.asList(book.getAuthor())))
                .andExpect(model().attribute("publishers", Arrays.asList(book.getPublisher())));
    }

    @Test
    void saveNewBook_NoValuesNoCover_BindingResults() throws Exception {
        Book book = createBook();

        MockMultipartFile coverImg = new MockMultipartFile("coverImg", "coverImg"
                , String.valueOf(MediaType.MULTIPART_FORM_DATA), (byte[]) null);

        this.mockMvc
                .perform(multipart("/books/new/save")
                    .file(coverImg)
                    .flashAttr("book", book)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bookForm"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    void saveNewBook_WithValuesNoCoverBookAlreadyExists_ThrowException() throws Exception {
        Book book = setTypicalParams(createBook());

        MockMultipartFile coverImg = new MockMultipartFile("coverImg", "coverImg"
                , String.valueOf(MediaType.MULTIPART_FORM_DATA), (byte[]) null);

        when(bookService.createBook(book)).thenThrow(new EntityAlreadyExistException("Book Already Exists"));

        this.mockMvc
                .perform(multipart("/books/new/save")
                        .file(coverImg)
                        .flashAttr("book", book)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bookForm"))
                .andExpect(model().attribute("Exception", true))
                .andExpect(model().attribute("exceptionMessage", "Book Already Exists"));
    }

    @Test
    void saveNewBook_WithValuesWithCover_NormalBehavior() throws Exception {
        Book book = setTypicalParams(createBook());

        MockMultipartFile coverImg = createMockFile();

        this.mockMvc
                .perform(multipart("/books/new/save")
                        .file(coverImg)
                        .flashAttr("book", book)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/catalog"));
    }

    @Test
    void deleteBook_NoBook_ThrowException() throws Exception {
        when(bookService.deleteBookById(1l)).thenThrow(new EntityNotFoundException("No Such Book"));

        this.mockMvc
                .perform(post("/books/delete/1")
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bookCatalog"))
                .andExpect(model().attribute("Exception", true))
                .andExpect(model().attribute("exceptionMessage", "No Such Book"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void deleteBook_NoErrors_NormalBehavior() throws Exception {
        this.mockMvc
                .perform(post("/books/delete/1")
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/catalog"));
    }

    @Test
    void changeBook_NoErrors_NormalBehavior() throws Exception {
        Book book = createBook();

        when(bookService.getBookById(1l)).thenReturn(book);

        this.mockMvc
                .perform(get("/books/edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", book))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    void saveBookChanges_NoValuesWithCover_BindingResults() throws Exception {
        Book book = createBook();

        MockMultipartFile coverImg = createMockFile();

        this.mockMvc
                .perform(multipart("/books/edit/1/save")
                    .file(coverImg)
                    .flashAttr("book", book)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bookEdit"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    void saveBookChanges_NoValuesNoCover_BindingResults() throws Exception {
        Book book = createBook();

        MockMultipartFile coverImg = new MockMultipartFile("coverImg", "coverImg"
                , String.valueOf(MediaType.MULTIPART_FORM_DATA), (byte[]) null);

        this.mockMvc
                .perform(multipart("/books/edit/1/save")
                        .file(coverImg)
                        .flashAttr("book", book)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bookEdit"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    void saveBookChanges_WithValuesWithCoverBookAlreadyExists_ThrowException() throws Exception {
        Book book = setTypicalParams(createBook());

        MockMultipartFile coverImg = createMockFile();

        when(bookService.updateBook(book, 1l)).thenThrow(new EntityAlreadyExistException("Book Already Exists"));

        this.mockMvc
                .perform(multipart("/books/edit/1/save")
                        .file(coverImg)
                        .flashAttr("book", book)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bookEdit"))
                .andExpect(model().attribute("Exception", true))
                .andExpect(model().attribute("exceptionMessage", "Book Already Exists"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    void saveBookChanges_WithValuesWithCover_NormalBehavior() throws Exception {
        Book book = setTypicalParams(createBook());

        MockMultipartFile coverImg = createMockFile();

        this.mockMvc
                .perform(multipart("/books/edit/1/save")
                        .file(coverImg)
                        .flashAttr("book", book)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/catalog"));
    }

    private static Book createBook() {
        Book book = new Book();
        Publisher publisher = new Publisher();
        Author author = new Author();
        book.setAuthor(author);
        book.setPublisher(publisher);

        return book;
    }

    private static Book setTypicalParams(Book book) {
        book.setId(1);
        book.setDescription("description");
        book.setPublishingYear(1999);
        book.setSubTitle("subTitle");
        book.setTitle("title");

        return book;
    }

    private static MockMultipartFile createMockFile() throws IOException {
        InputStream ks1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/noCover.jpg");
        return new MockMultipartFile("coverImg", "coverImg", String.valueOf(MediaType.MULTIPART_FORM_DATA), ks1.readAllBytes());
    }
}