package com.naipy.alpha.modules.user.controllers;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AddressInput(
        @NotBlank String zipCode,
        @NotBlank String streetNumber,
        @NotBlank String complement) implements Serializable { }
