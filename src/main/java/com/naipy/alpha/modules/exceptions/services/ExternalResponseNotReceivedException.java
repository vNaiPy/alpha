package com.naipy.alpha.modules.exceptions.services;

public class ExternalResponseNotReceivedException extends RuntimeException {
    public ExternalResponseNotReceivedException(String reason) {
        super(reason);
    }
}
