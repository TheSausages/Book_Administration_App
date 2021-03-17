package com.example.BookAdministration.Entities;

import com.example.BookAdministration.Security.RegistrationValidation.ValidPassword;
import com.example.BookAdministration.Security.RegistrationValidation.ValidUsername;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ValidUsername
    private String username;

    @ValidPassword
    private String password;

    @ValidPassword
    private String matchingPassword;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.matchingPassword = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void encryptPasswords() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(this.password);
        this.matchingPassword = password;
    }
}
