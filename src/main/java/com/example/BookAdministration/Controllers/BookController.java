package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Services.BookService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/catalog")
    public String viewBookCatalog(Model model) {
        model.addAttribute("books", bookService.getAllBooks());

        return "bookCatalog";
    }

    @GetMapping(value = "/cover/{id}")
    public void showBookCover(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        Book book = bookService.getBookById(id);

        InputStream is = new ByteArrayInputStream(book.getCover());
        IOUtils.copy(is, response.getOutputStream());
    }

    @GetMapping(value = "/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());

        return "newBook";
    }

    @PostMapping(value = "/new/save")
    public String saveNewBook(Model model, @ModelAttribute Book book) {
        bookService.createBook(book);

        return "redirect:/books/catalog";
    }
}
