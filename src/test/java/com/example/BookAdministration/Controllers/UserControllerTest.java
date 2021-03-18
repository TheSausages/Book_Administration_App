package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.User;
import com.example.BookAdministration.Services.MyUserDetailsService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
    void registrationSave_NoErrors_NormalBehavior() throws Exception {
        User user = setTypicalParams(new User());
        when(userDetailsService.createNewUser(user)).thenReturn(user);

        this.mockMvc
                .perform(post("/registration/save")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void registrationSave_FailValidationUsername_ReturnToViewWithErrors() throws Exception {
        User user = setTypicalParams(new User());
        user.setUsername("");

        this.mockMvc
                .perform(post("/registration/save")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("registration"));
    }

    @Test
    void registrationSave_FailValidationPassword_ReturnToViewWithErrors() throws Exception {
        User user = setTypicalParams(new User());
        user.setPassword("");

        this.mockMvc
                .perform(post("/registration/save")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("registration"));
    }

    @Test
    void registrationSave_FailValidationMatchingPassword_ReturnToViewWithErrors() throws Exception {
        User user = setTypicalParams(new User());
        user.setMatchingPassword("");

        this.mockMvc
                .perform(post("/registration/save")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("registration"));
    }

    @Test
    void registrationSave_FailValidationAllValues_ReturnToViewWithErrors() throws Exception {
        User user = new User();
        user.setId(1l);
        user.setUsername("");
        user.setPassword("");
        user.setMatchingPassword("");

        this.mockMvc
                .perform(post("/registration/save")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("registration"));
    }


    private static User setTypicalParams(User user) {
        user.setId(1l);
        user.setUsername("Username1");
        user.setPassword("Password1");
        user.setMatchingPassword("Password1");

        return user;
    }
}