package com.naipy.alpha.modules.store.controller;

import com.naipy.alpha.modules.store.models.Store;
import com.naipy.alpha.modules.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StoreController {

    @Autowired
    private final StoreService _storeService;

    /*@MutationMapping
    @Secured("USER")
    public Store storeRegistration (@Argument StoreInput store) {

        Localization newAddress = Localization.builder()
                .street(store.address.street)
                .complement(store.address.complement)
                .neighborhood(store.address.neighborhood)
                .state(store.address.state)
                .city(store.address.city)
                .country(store.address.country)
                .longitude(store.address.longitude)
                .latitude(store.address.latitude)
                .build();

        Store newStore = Store.builder()
                .name(store.name)
                .logoUrl(store.logoUrl)
                .bannerUrl(store.bannerUrl)
                .instant(Instant.parse("2019-06-20T21:53:07Z"))
                .build();

        return _storeService.register(newStore, newAddress);
    }*/

    @QueryMapping
    public List<Store> findAllStore () {
        return _storeService.findAll();
    }

    @QueryMapping
    @Secured("USER")
    public Store findStoreByCurrentUser () {
        return _storeService.findStoreByCurrentUser();
    }

    record StoreInput (String name, String logoUrl, String bannerUrl, LocalizationInput address) {}
    record LocalizationInput (String street, String complement, String neighborhood, String city, String state, String country, Double longitude, Double latitude) {}
}
