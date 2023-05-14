package com.naipy.alpha.entities.dto;

import com.naipy.alpha.entities.Category;
import com.naipy.alpha.entities.Product;
import com.naipy.alpha.entities.Store;
import com.naipy.alpha.entities.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private ProductStatus status;
    private Store store;
    private Set<Category> categories = new HashSet<>();

    public static ProductDTO createProductDTO (Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .status(product.getStatus())
                .store(product.getOwner().getStore())
                .categories(product.getCategories())
                .build();
    }
}
