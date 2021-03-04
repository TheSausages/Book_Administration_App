package com.example.BookAdministration;

import com.example.BookAdministration.Controllers.AuthorController;
import com.example.BookAdministration.Controllers.BookController;
import com.example.BookAdministration.Controllers.PublisherController;
import com.example.BookAdministration.Controllers.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookAdministrationApplicationTests {

	@Autowired
	private AuthorController authorController;

	@Autowired
	private BookController bookController;

	@Autowired
	private PublisherController publisherController;

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		assertThat(authorController).isNotNull();
		assertThat(bookController).isNotNull();
		assertThat(publisherController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
