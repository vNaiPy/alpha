package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.User;

import com.naipy.alpha.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService _userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll () {
        List<User> userList = _userService.findAll();
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById (@PathVariable Long id) {
        User user = _userService.findById(id);
        return ResponseEntity.ok().body(user);
    }
}
