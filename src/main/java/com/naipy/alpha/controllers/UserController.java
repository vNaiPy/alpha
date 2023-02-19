package com.naipy.alpha.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naipy.alpha.entities.User;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @GetMapping
    public ResponseEntity<User> findAll () {
        User user = new User(1L, "Carlos", "carlos@gmail.com", "11992091129", "123qwe");
        return ResponseEntity.ok().body(user);
    }

}
