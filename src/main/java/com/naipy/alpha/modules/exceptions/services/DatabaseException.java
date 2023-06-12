package com.naipy.alpha.modules.exceptions.services;

public class DatabaseException extends RuntimeException {
    public DatabaseException (String message) {
        super(message);
    }
}
