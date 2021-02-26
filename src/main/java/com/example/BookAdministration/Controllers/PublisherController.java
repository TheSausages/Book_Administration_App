package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "publishers")
public class PublisherController {
    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
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
    public String saveNewPublisher(@Valid @ModelAttribute Publisher publisher,@PathVariable Boolean whatSite , Model model) {
        publisherService.createPublisher(publisher);

        if (whatSite) {
            return "redirect:/books/new";
        } else {
            return "redirect:/publishers/list";
        }
    }
}
