package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Store;
import com.naipy.alpha.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StoreController {

    @Autowired
    private final StoreService _storeService;

    @MutationMapping
    @Secured("USER")
    public Store storeRegistration (@Argument Store store) {
        return _storeService.register(store);
    }

    @QueryMapping
    public List<Store> findAllStore () {
        return _storeService.findAll();
    }

    @QueryMapping
    @Secured("USER")
    public Store findStoreByCurrentUser () {
        return _storeService.findStoreByCurrentUser();
    }
}
