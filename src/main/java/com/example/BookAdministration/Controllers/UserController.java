package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Services.MyUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping(value = "/loginError")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);

        return "home";
    }
}
