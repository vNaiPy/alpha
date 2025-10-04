package com.naipy.alpha.modules.product.model;

import com.naipy.alpha.modules.utils.models.PaginatorInput;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record SearchingProductInput(
        String searchingFor,
        List<String> categoryIds,
        @NotNull Double lng,
        @NotNull Double lat,
        Double radius,
        @NotNull PaginatorInput paginatorInput
) implements Serializable {
}
