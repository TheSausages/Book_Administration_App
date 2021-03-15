package com.example.BookAdministration.Exceptions;

public class EntityHasChildrenException extends RuntimeException {

    public EntityHasChildrenException(String message) {
        super(message);
    }
}
