package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Services.BookService;
import com.example.BookAdministration.Services.PublisherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "publishers")
public class PublisherController {

    private final PublisherService publisherService;
    private final BookService bookService;

    @Autowired
    public PublisherController(PublisherService publisherService, BookService bookService) {
        this.publisherService = publisherService;
        this.bookService = bookService;
    }

    @GetMapping(value = "/list")
    public String viewPublisherList(Model model) {
        model.addAttribute("publishers", publisherService.getAllPublishers());

        return "publisherList";
    }

    @GetMapping(value = "/new/{whatSite}")
    public String newPublisher(@PathVariable Boolean whatSite, Model model) {
        model.addAttribute("publisher", new Publisher());
        model.addAttribute("whatSite", whatSite);

        return "publisherForm";
    }

    @PostMapping(value = "/new/save/{whatSite}")
    public String saveNewPublisher(@Valid @ModelAttribute Publisher publisher, BindingResult bindingResult, @PathVariable Boolean whatSite , Model model) {
        if (bindingResult.hasErrors()) {
            return "publisherForm";
        }

        publisherService.createPublisher(publisher);

        if (whatSite) {
            return "redirect:/books/new";
        }

        return "redirect:/publishers/list";
    }

    @PostMapping(value = "/delete/{id}")
    public String deletePublisher(@PathVariable Long id, Model model) {
        bookService.checkIfAnyBooksByPublisherId(id);

        publisherService.deletePublisherById(id);

        return "redirect:/publishers/list";
    }

    @GetMapping(value = "/edit/{id}")
    public String changePublisher(@PathVariable Long id, Model model) {
        model.addAttribute("publisher", publisherService.getPublisherById(id));

        return "publisherEdit";
    }

    @PostMapping(value = "/edit/{id}/save")
    public String savePublisherChanges(@PathVariable Long id, @Valid @ModelAttribute Publisher publisher, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "publisherEdit";
        }

        publisherService.updatePublisher(publisher, id);

        return "redirect:/publishers/list";
    }
}
