package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.PrimaryGenre;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Services.AuthorService;
import com.example.BookAdministration.Services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;

@WebMvcTest(controllers = AuthorController.class)
@WithMockUser
class AuthorControllerTest {
    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void viewAuthorListTest() throws Exception {
        Author author = new Author();

        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(author));

        this.mockMvc
                .perform(get("/authors/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorList"))
                .andExpect(model().attributeExists("authors"));
    }

    @Test
    void viewAuthorTest() throws Exception {
        when(authorService.getAuthorById(1l)).thenReturn(new Author());

        this.mockMvc
                .perform(get("/authors/info/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorInfo"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void newAuthorFromBookFormTest() throws Exception {
        this.mockMvc
                .perform(get("/authors/new/true"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("whatSite", true));
    }

    @Test
    void newAuthorFromAuthorListTest() throws Exception {
        this.mockMvc
                .perform(get("/authors/new/false"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("whatSite", false));
    }

    @Test
    void saveNewAuthorThrowExceptionTest() throws Exception {
        Author authorWithValues = new Author();
        setTypicalParams(authorWithValues);

        when(authorService.createAuthor(authorWithValues)).thenThrow(new EntityAlreadyExistException("Author Already Exist"));

        MockMultipartFile portraitImg = createMockFile();

        this.mockMvc
                .perform(multipart("/authors/new/save/true")
                        .file(portraitImg)
                        .flashAttr("author", authorWithValues)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"))
                .andExpect(model().attribute("Exception", true))
                .andExpect(model().attribute("exceptionMessage", "Author Already Exist"));
    }



    @Test
    void saveNewAuthorNoValuesFromBookFormNoPortraitTest() throws Exception {
        Author authorNull = new Author();
        authorNull.setId(1);

        MockMultipartFile portraitImg = new MockMultipartFile("portraitImg", "portraitImg"
                , String.valueOf(MediaType.MULTIPART_FORM_DATA), (byte[]) null);

        this.mockMvc
                .perform(multipart("/authors/new/save/true")
                        .file(portraitImg)
                        .flashAttr("author", authorNull)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"));
    }

    @Test
    void saveNewAuthorNoValuesFromBookFormTest() throws Exception {
        Author authorNull = new Author();
        authorNull.setId(1);

        MockMultipartFile portraitImg = createMockFile();

        this.mockMvc
                .perform(multipart("/authors/new/save/true")
                    .file(portraitImg)
                    .flashAttr("author", authorNull)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"));
    }

    @Test
    void saveNewAuthorNoValuesFromAuthorListTest() throws Exception {
        Author authorNull = new Author();
        authorNull.setId(1);

        MockMultipartFile portraitImg = createMockFile();

        this.mockMvc
                .perform(multipart("/authors/new/save/false")
                        .file(portraitImg)
                        .flashAttr("author", authorNull)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"));
    }

    @Test
    void saveNewAuthorWithValuesFromBookFormTest() throws Exception {
        Author authorWithValues = new Author();
        setTypicalParams(authorWithValues);

        MockMultipartFile portraitImg = createMockFile();

        this.mockMvc
                .perform(multipart("/authors/new/save/true")
                        .file(portraitImg)
                        .flashAttr("author", authorWithValues)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/new"));
    }

    @Test
    void saveNewAuthorWithValuesFromAuthorListTest() throws Exception {
        Author authorWithValues = new Author();
        setTypicalParams(authorWithValues);

        MockMultipartFile portraitImg = createMockFile();

        this.mockMvc
                .perform(multipart("/authors/new/save/false")
                        .file(portraitImg)
                        .flashAttr("author", authorWithValues)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors/list"));
    }


    @Test
    void deleteAuthorNoAuthorTest() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(new Author()));
        doThrow(new EntityNotFoundException("No such Author")).when(bookService).checkIfAnyBooksByAuthorId(1l);

        this.mockMvc
                .perform(post("/authors/delete/1")
                .with(csrf()))
                .andExpect(view().name("authorList"))
                .andExpect(model().attribute("Exception", true))
                .andExpect(model().attribute("exceptionMessage", "No such Author"))
                .andExpect(model().attributeExists("authors"));

    }

    @Test
    void deleteAuthorFoundAuthorTest() throws Exception {
        doNothing().when(bookService).checkIfAnyBooksByAuthorId(1l);

        this.mockMvc
                .perform(post("/authors/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors/list"));
    }

    @Test
    void changeAuthorTest() throws Exception {
        Author author = new Author();
        author.setId(1);

        when(authorService.getAuthorById(1l)).thenReturn(author);

        this.mockMvc
                .perform(get("/authors/edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("author", author))
                .andExpect(view().name("authorEdit"));
    }

    @Test
    void saveAuthorChangesAuthorNullTest() throws Exception {
        Author author = new Author();
        author.setId(1);

        MockMultipartFile portraitImg = createMockFile();

        this.mockMvc
                .perform(multipart("/authors/edit/1/save")
                        .file(portraitImg)
                        .flashAttr("author", author)
                        .param("primaryGenreSelected", PrimaryGenre.Fantasy.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authorEdit"));
    }

    @Test
    void saveAuthorChangesThrowExceptionTest() throws Exception {
        Author author = new Author();
        setTypicalParams(author);

        MockMultipartFile portraitImg = createMockFile();

        when(authorService.updateAuthor(author, 1l)).thenThrow(new EntityAlreadyExistException("Author Already Exists"));

        this.mockMvc
                .perform(multipart("/authors/edit/1/save")
                        .file(portraitImg)
                        .flashAttr("author", author)
                        .param("primaryGenreSelected", PrimaryGenre.Fantasy.toString())
                        .with(csrf()))
                .andExpect(view().name("authorEdit"))
                .andExpect(model().attribute("Exception", true))
                .andExpect(model().attribute("exceptionMessage", "Author Already Exists"));
    }

    @Test
    void saveAuthorChangesCorrectChangesTest() throws Exception {
        Author author = new Author();
        setTypicalParams(author);

        MockMultipartFile portraitImg = createMockFile();

        this.mockMvc
                .perform(multipart("/authors/edit/1/save")
                    .file(portraitImg)
                    .flashAttr("author", author)
                    .param("primaryGenreSelected", PrimaryGenre.Fantasy.toString())
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors/list"));
    }

    @Test
    void saveAuthorChangesCorrectChangesNoPortraitTest() throws Exception {
        Author author = new Author();
        setTypicalParams(author);

        MockMultipartFile portraitImg = new MockMultipartFile("portraitImg", "portraitImg", String.valueOf(MediaType.MULTIPART_FORM_DATA), (byte[]) null);

        this.mockMvc
                .perform(multipart("/authors/edit/1/save")
                        .file(portraitImg)
                        .flashAttr("author", author)
                        .param("primaryGenreSelected", PrimaryGenre.Fantasy.toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors/list"));
    }



    private static void setTypicalParams(Author author) {
        author.setId(1);
        author.setFirstName("firstName");
        author.setLastName("lastName");
        author.setDateOfBirth(LocalDate.of(1999,9,26));
        author.setPrimaryGenre(PrimaryGenre.Fantasy);
    }

    private static MockMultipartFile createMockFile() throws IOException {
        InputStream ks1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/noPortrait.jpg");
        return new MockMultipartFile("portraitImg", "portraitImg", String.valueOf(MediaType.MULTIPART_FORM_DATA), ks1.readAllBytes());
    }
}