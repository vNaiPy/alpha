package com.naipy.alpha.modules.user.exceptions;

public class UserCannotBeAuthenticateException extends RuntimeException {
    public UserCannotBeAuthenticateException(String message) {
        super(message);
    }
}
