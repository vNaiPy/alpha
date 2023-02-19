package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @GetMapping
    public ResponseEntity<User> findAll () {
        User user = new User(1L, "Carlos", "carlos@gmail.com", "11992091129", "123qwe");
        return ResponseEntity.ok().body(user);
    }

}
