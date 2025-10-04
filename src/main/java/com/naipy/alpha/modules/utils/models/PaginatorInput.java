package com.naipy.alpha.modules.utils.models;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record PaginatorInput (
        @NotNull Integer page,
        @NotNull Integer size,
        String sortBy
) implements Serializable {
}
