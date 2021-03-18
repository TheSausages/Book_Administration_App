package com.example.BookAdministration.Exceptions;

public class PasswordLengthException extends RuntimeException {

    public PasswordLengthException() {
        super("Entered password is too long!");
    }
}
