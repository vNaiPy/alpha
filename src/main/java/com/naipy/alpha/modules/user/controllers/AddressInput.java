package com.naipy.alpha.modules.user.controllers;

import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AddressInput(
        @NotBlank String zipCode,
        @NotBlank String streetNumber,
        @NotBlank String complement,
        @NotBlank AddressUsageType addressUsageType) implements Serializable { }
