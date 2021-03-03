package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.User;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.PasswordsNotMatchingException;
import com.example.BookAdministration.Services.MyUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {
    private final MyUserDetailsService myUserDetailsService;

    public UserController(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @GetMapping(value = "/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping(value = "/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping(value = "/registration/save")
    public String registrationSave(@Valid @ModelAttribute User user, @ModelAttribute String matchingPass, BindingResult bindingResult, Model model, Errors errors) {
        if (bindingResult.hasErrors()) {
            return "registration";
        } else {
            try {
                myUserDetailsService.createNewUser(user);
            } catch (EntityAlreadyExistException | PasswordsNotMatchingException e) {
                model.addAttribute("Exception", true);
                model.addAttribute("exceptionMessage", e.getMessage());
                return "registration";
            }

            return "redirect:/home";
        }
    }
}
