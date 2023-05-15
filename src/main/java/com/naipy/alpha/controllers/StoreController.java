package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Product;
import com.naipy.alpha.entities.Store;
import com.naipy.alpha.entities.dto.ProductDTO;
import com.naipy.alpha.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/stores")
@RequiredArgsConstructor
public class StoreController {

    @Autowired
    private final StoreService _storeService;

    @GetMapping
    public ResponseEntity<List<Store>> findAll () {
        List<Store> storeList = _storeService.findAll();
        return ResponseEntity.ok().body(storeList);
    }

    @GetMapping(value = "/mystore")
    public ResponseEntity<Store> findStoreByCurrentUser () {
        Store store = _storeService.findStoreByCurrentUser();
        return ResponseEntity.ok().body(store);
    }

    @PostMapping
    public ResponseEntity<Store> storeRegistration (@RequestBody Store store) {
        store = _storeService.register(store);
        URI location = UtilsForController.getURI(store.getId());
        return ResponseEntity.created(location).body(store);
    }
}
