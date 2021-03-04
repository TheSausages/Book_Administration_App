package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Services.AuthorService;
import com.example.BookAdministration.Services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.InputStream;
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

        when(authorService.getAllAuthors()).thenReturn(Arrays.asList(author));

        this.mockMvc
                .perform(get("/authors/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorList"))
                .andExpect(model().attributeExists("authors"));
    }

    @Test
    void viewAuthor() throws Exception {
        Author author = new Author();

        when(authorService.getAuthorById(1l)).thenReturn(author);

        this.mockMvc
                .perform(get("/authors/info/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorInfo"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void newAuthor() throws Exception {
        this.mockMvc
                .perform(get("/authors/new/true"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("whatSite", true));

        this.mockMvc
                .perform(get("/authors/new/false"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorForm"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("whatSite", false));
    }

    @Test
    void saveNewAuthor() throws Exception {
        Author author = new Author();

        InputStream ks1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/noPortrait.jpg");
        MockMultipartFile portraitImg = new MockMultipartFile("portraitImg", "portraitImg", String.valueOf(MediaType.MULTIPART_FORM_DATA), ks1.readAllBytes());

        MockMultipartFile jsonFile = new MockMultipartFile("author", "", "application/json", toJsonString(author).getBytes());

        this.mockMvc
                .perform(multipart("/authors/new/save/true")
                .file(portraitImg)
                        .file(jsonFile)
                        .with(csrf()))
                .andExpect(status().isOk());

        /*this.mockMvc
                .perform(post("/authors/new/save/true").with(csrf())
                .accept(MediaType.MULTIPART_FORM_DATA)
                .param("author", toJsonString(author))
                .requestAttr("portraitImg", portraitImg)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());*/

        /*this.mockMvc
                .perform(multipart("/authors/new/save/true")
                .file(mockMultipartFile)
                .param("author", toJsonString(author))
                .with(csrf()))
                .andExpect(status().isOk());*/
    }

    /*@Test
    void deleteAuthor() {
    }

    @Test
    void changeAuthor() {
    }

    @Test
    void saveAuthorChanges() {
    }*/

    public static String toJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}