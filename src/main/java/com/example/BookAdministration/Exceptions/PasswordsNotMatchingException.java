package com.example.BookAdministration.Exceptions;

public class PasswordsNotMatchingException extends RuntimeException {

    public PasswordsNotMatchingException() {
        super("Entered passwords are not the Same!");
    }
}
