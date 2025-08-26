package com.naipy.alpha.modules.user.models;

import jakarta.validation.constraints.NotNull;

public record RegisterRequest (
    @NotNull String name,
    @NotNull String surname,
    @NotNull String email,
    @NotNull String identityDocument,
    @NotNull String phone,
    @NotNull String password
) { }
