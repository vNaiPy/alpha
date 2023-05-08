package com.naipy.alpha.controllers;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UtilsForController {

    public static URI getURI (Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

}
