package com.naipy.alpha.modules.product.model;

import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.store.models.StoreDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public record ProductDTO (
        String id,
        String name,
        String description,
        Double price,
        String imgUrl,
        ProductStatus status,
        StoreDTO storeDTO,
        Set<Category> categories
) implements Serializable {
    public static ProductDTO createProductDTO (Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl(),
                product.getStatus(),
                StoreDTO.createStoreDTO(product.getOwner().getStore()),
                !product.getCategories().isEmpty() ? product.getCategories() : new HashSet<>()
        );
    }
}
