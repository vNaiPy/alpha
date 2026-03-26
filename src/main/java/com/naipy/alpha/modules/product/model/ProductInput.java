package com.naipy.alpha.modules.product.model;

import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.product.enums.ProductStatus;

import java.io.Serializable;
import java.util.Set;

public record ProductInput  (
        String id,
        String name,
        String description,
        Double price,
        String imgUrl,
        ProductStatus status,
        Set<Category> categories
) implements Serializable {
}
