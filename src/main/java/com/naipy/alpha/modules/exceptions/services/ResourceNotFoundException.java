package com.naipy.alpha.modules.exceptions.services;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException (String message) {
        super("Resource not found. " + message);
    }
}
