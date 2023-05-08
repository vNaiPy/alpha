package com.naipy.alpha.controllers;

import com.naipy.alpha.entities.User;

import com.naipy.alpha.entities.dto.UserDTO;
import com.naipy.alpha.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService _userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll () {
        List<UserDTO> userList = _userService.findAll();
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById (@PathVariable Long id) {
        UserDTO userDTO = _userService.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping
    public ResponseEntity<User> insert (@RequestBody User user) {
        user = _userService.insert(user);
        URI uri = UtilsForController.getURI(user.getId());
        return ResponseEntity.created(uri).body(user);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        _userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> update (@PathVariable Long id, @RequestBody User user) {
        user = _userService.update(id, user);
        return ResponseEntity.ok().body(user);
    }

}
