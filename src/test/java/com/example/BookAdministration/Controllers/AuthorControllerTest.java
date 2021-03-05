package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.PrimaryGenre;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityHasChildrenException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Services.AuthorService;
import com.example.BookAdministration.Services.BookService;
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
    void viewAuthorList_Test() throws Exception {
        Author author = new Author();

        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(author));

        this.mockMvc
                .perform(get("/authors/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorList"))
                .andExpect(model().attributeExists("authors"));
    }

    @Test
    void viewAuthor_NoErrors_NormalBehavior() throws Exception {
        Author author = new Author();
        setTypicalParams(author);

        when(authorService.getAuthorById(1l)).thenReturn(author);

        this.mockMvc
                .perform(get("/authors/info/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorInfo"))
                .andExpect(model().attribute("author", author))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void newAuthor_FromBookForm_NormalBehavior() throws Exception {
        this.mockMvc
                .perform(get("/authors/new/true"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("whatSite", true));
    }

    @Test
    void newAuthor_FromAuthorList_NormalBehavior() throws Exception {
        this.mockMvc
                .perform(get("/authors/new/false"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("whatSite", false));
    }

    @Test
    void saveNewAuthor_FromAuthorListTest_ThrowException() throws Exception {
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
    void saveNewAuthor_NoValuesNoPortraitFromBookForm_BindingResults() throws Exception {
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
    void saveNewAuthor_NoValuesFromBookForm_BindingResults() throws Exception {
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
    void saveNewAuthor_NoValuesFromAuthorList_BindingResults() throws Exception {
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
    void saveNewAuthor_WithValuesFromBookForm_NormalBehavior() throws Exception {
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
    void saveNewAuthor_WithValuesFromAuthorList_NormalBehavior() throws Exception {
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
    void deleteAuthor_AuthorHasChildren_ThrowException() throws Exception {
        doThrow(new EntityHasChildrenException("Author has books!")).when(bookService).checkIfAnyBooksByAuthorId(1l);

        this.mockMvc
                .perform(post("/authors/delete/1")
                    .with(csrf()))
                .andExpect(view().name("authorList"))
                .andExpect(model().attribute("Exception", true))
                .andExpect(model().attribute("exceptionMessage", "Author has books!"))
                .andExpect(model().attributeExists("authors"));
    }

    @Test
    void deleteAuthor_NoAuthor_ThrowException() throws Exception {
        doNothing().when(bookService).checkIfAnyBooksByAuthorId(1l);
        when(authorService.deleteAuthorById(1l)).thenThrow(new EntityNotFoundException("No such Author"));

        this.mockMvc
                .perform(post("/authors/delete/1")
                        .with(csrf()))
                .andExpect(view().name("authorList"))
                .andExpect(model().attribute("Exception", true))
                .andExpect(model().attribute("exceptionMessage", "No such Author"))
                .andExpect(model().attributeExists("authors"));
    }


    @Test
    void deleteAuthor_NoErrors_NormalBehavior() throws Exception {
        doNothing().when(bookService).checkIfAnyBooksByAuthorId(1l);

        this.mockMvc
                .perform(post("/authors/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors/list"));
    }

    @Test
    void changeAuthor_NoErrors_NormalBehavior() throws Exception {
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
    void saveAuthorChanges_NoValues_BindingResults() throws Exception {
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
    void saveAuthorChanges_AuthorAlreadyExists_ThrowException() throws Exception {
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
    void saveAuthorChanges_WithPortraitNoErrors_NormalBehavior() throws Exception {
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
    void saveAuthorChanges_NoPortraitNoErrors_NormalBehavior() throws Exception {
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