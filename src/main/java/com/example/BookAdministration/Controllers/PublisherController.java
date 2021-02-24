package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping(value = "/new")
    public String newPublisher(Model model) {
        model.addAttribute("publisher", new Publisher());

        return "newPublisher";
    }

    @PostMapping(value = "/new/save")
    public String saveNewPublisher(@Valid @ModelAttribute Publisher publisher, Model model) {
        publisherService.createPublisher(publisher);

        return "redirect:/publishers/list";
    }
}
