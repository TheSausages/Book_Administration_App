package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.User;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Exceptions.PasswordsNotMatchingException;
import com.example.BookAdministration.Repositories.UserRepository;
import com.example.BookAdministration.Security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Load user with username:" + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No such User!"));

        return new UserPrincipal(user);
    }

    public void createNewUser(User user) {
        logger.info("Register new User");

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new EntityAlreadyExistException("This username is already taken!");
        }

        if (!user.getPassword().equals(user.getMatchingPassword())) {
            throw new PasswordsNotMatchingException("Password are not the same!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMatchingPassword(user.getPassword());

        userRepository.save(user);
    }
}
