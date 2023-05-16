package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Product;
import com.naipy.alpha.entities.Store;
import com.naipy.alpha.entities.dto.ProductDTO;
import com.naipy.alpha.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StoreController {

    @Autowired
    private final StoreService _storeService;

    @QueryMapping
    public List<Store> findAll () {
        return _storeService.findAll();
    }

    @GetMapping(value = "/mystore")
    public ResponseEntity<Store> findStoreByCurrentUser () {
        Store store = _storeService.findStoreByCurrentUser();
        return ResponseEntity.ok().body(store);
    }

    @MutationMapping
    public Store storeRegistration (@Argument Store store) {
        return _storeService.register(store);
    }
}
