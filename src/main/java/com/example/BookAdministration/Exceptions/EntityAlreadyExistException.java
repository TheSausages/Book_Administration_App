package com.example.BookAdministration.Exceptions;

public class EntityAlreadyExistException extends RuntimeException{

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
