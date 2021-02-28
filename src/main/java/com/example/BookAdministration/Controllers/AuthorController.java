package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Entities.PrimaryGenre;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Services.AuthorService;
import com.example.BookAdministration.Services.BookService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping(value = "/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping(value = "/list")
    public String viewAuthorList(Model model) {
        Map<Author, List<Book>> authorsWithBooks = new HashMap<>();

        authorService.getAllAuthors().forEach(author -> {
            authorsWithBooks.put(author, bookService.get3BooksByAuthorId(author.getId()));
        });

        model.addAttribute("authors", authorsWithBooks);

        return "AuthorList";
    }

    @GetMapping(value = "/portrait/{id}")
    public void showBookCover(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");

        Author author = authorService.getAuthorById(id);

        InputStream is = new ByteArrayInputStream(author.getPortrait());
        IOUtils.copy(is, response.getOutputStream());
    }

    @GetMapping(value = "/new/{whatSite}")
    public String newAuthor(@PathVariable Boolean whatSite, Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("genres", PrimaryGenre.values());
        model.addAttribute("whatSite", whatSite);

        return "authorForm";
    }

    @PostMapping(value = "/new/save/{whatSite}", consumes = "multipart/form-data")
    public String saveNewAuthor(@Valid @ModelAttribute Author author, BindingResult bindingResult, @RequestParam("portraitImg") MultipartFile file, @PathVariable Boolean whatSite, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "authorForm";
        } else {
            if (!file.isEmpty()) {
                author.setPortrait(file.getBytes());
            } else {
                InputStream noPortrait = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/NoPortrait.jpg"));
                author.setPortrait(noPortrait.readAllBytes());
            }

            try {
                authorService.createAuthor(author);
            } catch (EntityAlreadyExistException e) {
                model.addAttribute("Exception", true);
                model.addAttribute("exceptionMessage", e.getMessage());
                return "authorForm";
            }

            if (whatSite) {
                return "redirect:/books/new";
            } else {
                return "redirect:/authors/list";
            }
        }
    }
}
