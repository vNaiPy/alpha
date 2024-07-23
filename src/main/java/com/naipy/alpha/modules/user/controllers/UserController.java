package com.naipy.alpha.modules.user.controllers;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.user.models.*;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.service.UserAddressService;
import com.naipy.alpha.modules.utils.ControllerUtils;

import com.naipy.alpha.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService _userService;

    @Secured({"ADMIN"})
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll () {
        List<UserDTO> userList = _userService.findAll();
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById (@PathVariable UUID id) {
        UserDTO userDTO = _userService.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> addNewUser (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(_userService.addNewUser(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(_userService.authenticate(request));
    }

    @MutationMapping
    @Secured("USER")
    public UserAddress addAddressToUser (@Argument AddressInput addressInput) {
        return _userService.addAddressToUser(addressInput);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> update (@PathVariable UUID id, @RequestBody User user) {
        user = _userService.update(id, user);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete (@PathVariable UUID id) {
        _userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
