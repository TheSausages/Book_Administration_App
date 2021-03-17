package com.example.BookAdministration.Exceptions;

public class PasswordLengthException extends RuntimeException {

    public PasswordLengthException(String message) {
        super(message);
    }
}
