package com.naipy.alpha.controllers.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService _authService;

    @MutationMapping
    public AuthenticationResponse register (@Argument RegisterRequest request) {
        return _authService.register(request);
    }

    @MutationMapping
    public AuthenticationResponse authenticate (@Argument AuthenticationRequest request) {
        return _authService.authenticate(request);
    }
}
