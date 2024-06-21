package com.naipy.alpha.modules.exceptions.services;

public class GenericErrorException extends Exception {
    public GenericErrorException () {
        super("Unmapped error.");
    }
}
