package com.naipy.alpha.modules.user.models;


import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AuthenticationResponse (
        @NotBlank String token
) implements Serializable {}
