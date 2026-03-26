package com.naipy.alpha.modules.store.models;

import com.naipy.alpha.modules.store.enums.StoreStatus;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record StoreDTO (
        String id,
        String name,
        String logoUrl,
        String bannerUrl,
        String description,
        Instant createdAt,
        StoreStatus storeStatus
) implements Serializable {
    public static StoreDTO createStoreDTO(Store store) {
        return new StoreDTO(
                store.getId(),
                store.getName(),
                store.getLogoUrl(),
                store.getBannerUrl(),
                store.getDescription(),
                store.getCreatedAt(),
                store.getStoreStatus()
        );
    }

    public static List<StoreDTO> storePageToStoreDTOList (Page<Store> storePage) {
        return storePage.stream().map(StoreDTO::createStoreDTO).toList();
    }
}
