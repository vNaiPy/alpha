package com.naipy.alpha.modules.store.controller;

import com.naipy.alpha.modules.store.models.Store;
import com.naipy.alpha.modules.store.models.StoreDTO;
import com.naipy.alpha.modules.store.service.StoreService;
import com.naipy.alpha.modules.user.controllers.AddressInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class StoreController {

    private final StoreService _storeService;

    @Autowired
    public StoreController(StoreService storeService) { this._storeService = storeService; }

    @MutationMapping
    @Secured("USER")
    public Store registerStore (@Argument StoreDTO store, @Argument AddressInput addressInput) {
        return _storeService.register(store, addressInput);
    }

    @QueryMapping
    public List<StoreDTO> findAllStore () {
        return _storeService.findAll();
    }

    @QueryMapping
    public StoreDTO findStoreByName (@Argument final String name) {
        return _storeService.findByName(name);
    }

    @QueryMapping
    public List<StoreDTO> findAllStoreByNameContaining (@Argument final String name) {
        return _storeService.findByNameContaining(name);
    }

    @QueryMapping
    public StoreDTO findStoreById(@Argument final String id) {
        return _storeService.findById(id);
    }

    @QueryMapping
    @Secured("USER")
    public StoreDTO findStoreByCurrentUser () {
        return _storeService.findStoreByCurrentUser();
    }

    @MutationMapping
    @Secured("USER")
    public String desactivateStore () {
        StoreDTO desactivatedStore = _storeService.desactivate();
        return "Store with name: ".concat(desactivatedStore.name()).concat(" has been desactivated.");
    }
}
