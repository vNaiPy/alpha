package com.naipy.alpha.modules.utils.models;

import java.io.Serializable;

public record PaginatorInput (
        Integer page,
        Integer size,
        String sortBy
) implements Serializable {
}
