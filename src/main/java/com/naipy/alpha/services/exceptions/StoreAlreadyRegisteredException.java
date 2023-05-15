package com.naipy.alpha.services.exceptions;

public class StoreAlreadyRegisteredException extends RuntimeException {
    public StoreAlreadyRegisteredException(String message) {
        super(message);
    }
}
