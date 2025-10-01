package com.naipy.alpha.modules.product.model;

import com.naipy.alpha.modules.utils.models.PaginatorInput;

public record SearchingProductInput(
        String searchingFor,
        Double lng,
        Double lat,
        Double radius,
        PaginatorInput paginatorInput
) {
}
