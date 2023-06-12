package com.naipy.alpha.modules.exceptions.services;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException (Object id) {
        super("Resource not found. Id: " + id);
    }
}
