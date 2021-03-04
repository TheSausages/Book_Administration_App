package com.example.BookAdministration.Controllers;


import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Services.AuthorService;
import com.example.BookAdministration.Services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import static org.mockito.Mockito.when;

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
        author.setFirstName("FirstName");

        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(author));

        this.mockMvc
                .perform(get("/authors/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorList"))
                .andExpect(model().attributeExists("authors"));
    }

    /*@Test
    void showAuthorPortrait() {
    }

    @Test
    void viewAuthor() {
    }

    @Test
    void newAuthor() {
    }

    @Test
    void saveNewAuthor() {
    }

    @Test
    void deleteAuthor() {
    }

    @Test
    void changeAuthor() {
    }

    @Test
    void saveAuthorChanges() {
    }*/
}