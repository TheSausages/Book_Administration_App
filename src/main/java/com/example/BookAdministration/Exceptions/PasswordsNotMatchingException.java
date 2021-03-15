package com.example.BookAdministration.Exceptions;

public class PasswordsNotMatchingException extends RuntimeException {

    public PasswordsNotMatchingException(String message) {
        super(message);
    }
}
