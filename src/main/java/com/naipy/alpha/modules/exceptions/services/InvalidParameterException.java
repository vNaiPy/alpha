package com.naipy.alpha.modules.exceptions.services;

public class InvalidParameterException extends IllegalArgumentException {
    public InvalidParameterException (Object param) {
        super("Parameter is invalid. Param: " + param);
    }
}
