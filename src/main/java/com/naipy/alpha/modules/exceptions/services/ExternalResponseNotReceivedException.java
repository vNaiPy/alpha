package com.naipy.alpha.modules.exceptions.services;

public class ExternalResponseNotReceivedException extends RuntimeException {
    public ExternalResponseNotReceivedException(Object codeReceived) {
        super("Geocode response not received! Code received from external API: " + codeReceived);
    }
}
