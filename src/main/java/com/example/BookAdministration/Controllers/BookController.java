package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityHasChildrenException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Services.AuthorService;
import com.example.BookAdministration.Services.BookService;
import com.example.BookAdministration.Services.PublisherService;
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

@Controller
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService, PublisherService publisherService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    @GetMapping(value = "/catalog")
    public String viewBookCatalog(Model model) {
        model.addAttribute("books", bookService.getAllBooks());

        return "bookCatalog";
    }

    @GetMapping(value = "/cover/{id}")
    public void showBookCover(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("image/*");

        Book book = bookService.getBookById(id);

        InputStream is = new ByteArrayInputStream(book.getCover());
        IOUtils.copy(is, response.getOutputStream());
    }

    @GetMapping(value = "/info/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));

        return "bookInfo";
    }

    @GetMapping(value = "/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("publishers", publisherService.getAllPublishers());

        return "bookForm";
    }

    @PostMapping(value = "/new/save", consumes = "multipart/form-data")
    public String saveNewBook(@Valid @ModelAttribute Book book, BindingResult bindingResult, @RequestParam("coverImg") MultipartFile file, Model model) throws IOException {
        if (bindingResult.hasErrors()) {

            //when reloading these attributes where lost so need to add again
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("publishers", publisherService.getAllPublishers());
            return "bookForm";
        } else {
            if (!file.isEmpty()) {
                book.setCover(file.getBytes());
            } else {
                InputStream noCover = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/NoCover.jpg"));
                book.setCover(noCover.readAllBytes());
            }

            try {
                bookService.createBook(book);
            } catch (EntityAlreadyExistException e) {
                model.addAttribute("Exception", true);
                model.addAttribute("exceptionMessage", e.getMessage());
                return "bookForm";
            }
        }

        return "redirect:/books/catalog";
    }

    @PostMapping(value = "/delete/{id}")
    public String deletePublisher(@PathVariable Long id, Model model) {
        try {
            bookService.deleteBookById(id);
        } catch (EntityNotFoundException | EntityHasChildrenException e) {
            model.addAttribute("Exception", true);
            model.addAttribute("exceptionMessage", e.getMessage());
            model.addAttribute("books", bookService.getAllBooks());
            return "bookCatalog";
        }

        return "redirect:/books/catalog";
    }

    @GetMapping(value = "/edit/{id}")
    public String changePublisher(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));

        return "bookEdit";
    }

    @PostMapping(value = "/edit/{id}/save")
    public String savePublisherChanges(@PathVariable Long id, @Valid @ModelAttribute Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "bookEdit";
        } else {
            try {
                bookService.updateBook(book, id);
            } catch (EntityAlreadyExistException e) {
                model.addAttribute("Exception", true);
                model.addAttribute("exceptionMessage", e.getMessage());
                return "bookEdit";
            }

            return "redirect:/books/catalog";
        }
    }
}
