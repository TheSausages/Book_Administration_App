package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityHasChildrenException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
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
        } else {
            try {
                publisherService.createPublisher(publisher);
            } catch (EntityAlreadyExistException e) {
                model.addAttribute("Exception", true);
                model.addAttribute("exceptionMessage", e.getMessage());
                return "publisherForm";
            }

            if (whatSite) {
                return "redirect:/books/new";
            } else {
                return "redirect:/publishers/list";
            }
        }
    }

    @PostMapping(value = "/delete/{id}")
    public String deletePublisher(@PathVariable Long id, Model model) {
        try {
            bookService.checkIfAnyBooksByPublisherId(id);

            publisherService.deletePublisherById(id);
        } catch (EntityNotFoundException | EntityHasChildrenException e) {
            model.addAttribute("Exception", true);
            model.addAttribute("exceptionMessage", e.getMessage());
            model.addAttribute("publishers", publisherService.getAllPublishers());
            return "publisherList";
        }

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
        } else {
            try {
                publisherService.updatePublisher(publisher, id);
            } catch (EntityAlreadyExistException e) {
                model.addAttribute("Exception", true);
                model.addAttribute("exceptionMessage", e.getMessage());
                return "publisherEdit";
            }

            return "redirect:/publishers/list";
        }
    }
}
