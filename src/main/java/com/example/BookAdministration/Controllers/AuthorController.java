package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Entities.PrimaryGenre;
import com.example.BookAdministration.Services.AuthorService;
import com.example.BookAdministration.Services.BookService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/new")
    public String newAuthor(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("genres", PrimaryGenre.values());

        return "newAuthor";
    }

    @PostMapping(value = "/new/save")
    public String saveNewAuthor(@Valid @ModelAttribute Author author, Model model) {
        authorService.createAuthor(author);

        return "redirect:/authors/list";
    }
}
