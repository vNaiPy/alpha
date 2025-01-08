package com.naipy.alpha.modules.exceptions.services;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class InvalidParameterException extends IllegalArgumentException {
    public InvalidParameterException (Object param) {
        super("Parameter is invalid. Param: " + param);
    }
}
