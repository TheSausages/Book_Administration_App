package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.User;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Exceptions.PasswordsNotMatchingException;
import com.example.BookAdministration.Repositories.UserRepository;
import com.example.BookAdministration.Security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest

class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_NoSuchUser_throwException() {
        //given
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("user");
        });

        //then
        assertEquals("No such User!", exception.getMessage());
    }

    @Test
    void loadUserByUsername_NoErrors_NormalBehavior() {
        //given
        User user = new User("user", "pass");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        //when
        UserDetails userDetailsReturned = userDetailsService.loadUserByUsername("user");

        //then
        assertEquals(userDetailsReturned.getUsername(), user.getUsername());
    }

    @Test
    void createNewUser_UserAlreadyExists_ThrowException() {
        //given
        User user = new User("user", "pass");
        when(userRepository.existsByUsername("user")).thenReturn(true);

        //when
        RuntimeException exception = assertThrows(EntityAlreadyExistException.class, () -> {
            userDetailsService.createNewUser(user);
        });

        //then
        assertEquals("This username is already taken!", exception.getMessage());
    }

    @Test
    void createNewUser_PasswordsDoNotMatch_ThrowException() {
        //given
        User user = new User("user", "pass");
        user.setMatchingPassword("passs");

        //when
        RuntimeException exception = assertThrows(PasswordsNotMatchingException.class, () -> {
            userDetailsService.createNewUser(user);
        });

        //then
        assertEquals("Passwords are not the same!", exception.getMessage());
    }

    @Test
    void createNewUser_NoErrors_NormalBehavior() {
        //given
        User user = new User("user", "pass");
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        //when
        User userReturned = userDetailsService.createNewUser(user);

        //Need to encode Password of user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMatchingPassword(user.getPassword());

        //then
        assertEquals(user, userReturned);
    }
}