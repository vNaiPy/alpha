package com.naipy.alpha.modules.exceptions.services;

public class StoreAlreadyRegisteredException extends RuntimeException {
    public StoreAlreadyRegisteredException(String message) {
        super(message);
    }
}
