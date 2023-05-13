package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.Store;
import com.naipy.alpha.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mystore")
@RequiredArgsConstructor
public class StoreController {

    @Autowired
    private final StoreService _storeService;

    @GetMapping
    public ResponseEntity<Store> findStoreByCurrentUser () {
        Store userDTO = _storeService.findStoreByCurrentUser();
        return ResponseEntity.ok().body(userDTO);
    }

}
