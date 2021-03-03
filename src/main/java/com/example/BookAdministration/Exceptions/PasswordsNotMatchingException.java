package com.example.BookAdministration.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PasswordsNotMatchingException extends RuntimeException{

    public PasswordsNotMatchingException(String message) {
        super(message);
    }
}
