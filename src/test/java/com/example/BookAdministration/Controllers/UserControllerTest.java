package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.User;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.PasswordsNotMatchingException;
import com.example.BookAdministration.Services.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@WithMockUser
class UserControllerTest {

    @MockBean
    private MyUserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePage_NoErrors_NormalBehavior() throws Exception {
        this.mockMvc
                .perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void registration_NoErrors_NormalBehavior() throws Exception {
        this.mockMvc
                .perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void registrationSave_NoValues_BindingResults() throws Exception {
        User user = new User();

        this.mockMvc
                .perform(post("/registration/save")
                    .flashAttr("user", user)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    void registrationSave_UserAlreadyExists_ThrowError() throws Exception {
        User user = setTypicalParams(new User());

        when(userDetailsService.createNewUser(user)).thenThrow(new EntityAlreadyExistException("User Already Exists!"));

        this.mockMvc
                .perform(post("/registration/save")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registration"))
                .andExpect(flash().attribute("Exception", true))
                .andExpect(flash().attribute("exceptionMessage", "User Already Exists!"));
    }

    @Test
    void registrationSave_PasswordNotMatching_ThrowError() throws Exception {
        User user = setTypicalParams(new User());

        when(userDetailsService.createNewUser(user)).thenThrow(new PasswordsNotMatchingException("The Passwords are not the same!"));

        this.mockMvc
                .perform(post("/registration/save")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registration"))
                .andExpect(flash().attribute("Exception", true))
                .andExpect(flash().attribute("exceptionMessage", "The Passwords are not the same!"));
    }

    @Test
    void registrationSave_NoErrors_NormalBehavior() throws Exception {
        User user = setTypicalParams(new User());

        this.mockMvc
                .perform(post("/registration/save")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    private static User setTypicalParams(User user) {
        user.setId(1l);
        user.setUsername("user");
        user.setPassword("pass");
        user.setMatchingPassword("pass");

        return user;
    }
}