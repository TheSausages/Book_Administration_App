package com.example.BookAdministration.Controllers;

import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Services.BookService;
import com.example.BookAdministration.Services.MyUserDetailsService;
import com.example.BookAdministration.Services.PublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PublisherController.class)
@WithMockUser
class PublisherControllerTest {

    @MockBean
    private PublisherService publisherService;

    @MockBean
    private BookService bookService;

    @MockBean
    private MyUserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void viewPublisherList_NoErrors_NormalBehavior() throws Exception {
        Publisher publisher = new Publisher();

        when(publisherService.getAllPublishers()).thenReturn(Arrays.asList(publisher));

        this.mockMvc
                .perform(get("/publishers/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisherList"))
                .andExpect(model().attributeExists("publishers"));
    }

    @Test
    void newPublisher_FromBookFormNoErrors_NormalBehavior() throws Exception {
        this.mockMvc
                .perform(get("/publishers/new/true"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisherForm"))
                .andExpect(model().attributeExists("publisher"))
                .andExpect(model().attribute("whatSite", true));
    }

    @Test
    void newPublisher_FromPublisherListNoErrors_NormalBehavior() throws Exception {
        this.mockMvc
                .perform(get("/publishers/new/false"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisherForm"))
                .andExpect(model().attributeExists("publisher"))
                .andExpect(model().attribute("whatSite", false));
    }

    @Test
    void saveNewPublisher_NoValuesFromBookForm_BindingErrors() throws Exception {
        Publisher publisher = new Publisher();

        this.mockMvc
                .perform(post("/publishers/new/save/true")
                    .flashAttr("publisher", publisher)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("publisherForm"));
    }

    @Test
    void saveNewPublisher_WithValuesFromBookForm_NormalBehavior() throws Exception {
        Publisher publisher = setTypicalParams(new Publisher());

        this.mockMvc
                .perform(post("/publishers/new/save/true")
                        .flashAttr("publisher", publisher)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/new"));
    }

    @Test
    void saveNewPublisher_WithValuesFromPublisherList_NormalBehavior() throws Exception {
        Publisher publisher = setTypicalParams(new Publisher());

        this.mockMvc
                .perform(post("/publishers/new/save/false")
                        .flashAttr("publisher", publisher)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publishers/list"));
    }

    @Test
    void changePublisher_NoErrors_NormalBehavior() throws Exception {
        Publisher publisher = new Publisher();

        when(publisherService.getPublisherById(1l)).thenReturn(publisher);

        this.mockMvc
                .perform(get("/publishers/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisherEdit"))
                .andExpect(model().attribute("publisher", publisher));
    }

    @Test
    void savePublisherChanges_NoValue_BindingError() throws Exception {
        Publisher publisher = new Publisher();

        this.mockMvc
                .perform(post("/publishers/edit/1/save")
                    .flashAttr("publisher", publisher)
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("publisherEdit"));
    }

    @Test
    void savePublisherChanges_WithValues_normalBehavior() throws Exception {
        Publisher publisher = setTypicalParams(new Publisher());

        this.mockMvc
                .perform(post("/publishers/edit/1/save")
                        .flashAttr("publisher", publisher)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publishers/list"));
    }

    private static Publisher setTypicalParams(Publisher publisher) {
        publisher.setId(1);
        publisher.setName("publisher");
        publisher.setCity("City");

        return publisher;
    }
}