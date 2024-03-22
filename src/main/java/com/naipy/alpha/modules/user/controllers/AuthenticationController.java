package com.naipy.alpha.modules.user.controllers;

import com.naipy.alpha.modules.user.models.AuthenticationRequest;
import com.naipy.alpha.modules.user.models.AuthenticationResponse;
import com.naipy.alpha.modules.user.service.AuthenticationService;
import com.naipy.alpha.modules.user.models.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationService _authService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(_authService.authenticate(request));
    }
}
