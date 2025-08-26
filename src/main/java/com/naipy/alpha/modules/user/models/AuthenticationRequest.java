package com.naipy.alpha.modules.user.models;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AuthenticationRequest (
        @NotBlank String username,
        @NotBlank String password
) implements Serializable { }
