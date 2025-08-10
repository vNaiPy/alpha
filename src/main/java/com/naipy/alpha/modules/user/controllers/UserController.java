package com.naipy.alpha.modules.user.controllers;

import com.naipy.alpha.modules.user.models.*;
import com.naipy.alpha.modules.user_address.models.UserAddress;

import com.naipy.alpha.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService _userService;

    @Autowired
    public UserController(UserService userService) {
        this._userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> addNewUser (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(_userService.addNewUser(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(_userService.authenticate(request));
    }

    @DeleteMapping(value = "/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<Void> delete (@PathVariable String id) {
        _userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //GraphQL Query to find all users
    @QueryMapping
    @Secured({"ADMIN"})
    public List<UserDTO> findAllUsers () {
        return _userService.findAll();
    }

    //GraphQL Query to find user by ID
    @QueryMapping
    @Secured("USER")
    public UserDTO findUserById (@Argument String id) {
        return _userService.findById(id);
    }

    @QueryMapping
    @Secured("USER")
    public UserDTO findUserByUsername (@Argument String username) {
        return _userService.findByUsername(username);
    }

    //GraphQL Query to find user by username containing a specific username
    @QueryMapping
    @Secured("USER")
    public List<UserDTO> findUserByUsernameContaining (@Argument String username) {
        return _userService.findByUsernameContaining(username);
    }

    //GraphQL Mutation to add an address to a user
    @MutationMapping
    @Secured("USER")
    public UserAddress addAddressToUser (@Argument AddressInput addressInput) {
        return _userService.addAddressToUser(addressInput);
    }

    //GraphQL Mutation to update a user
    @MutationMapping
    @Secured("USER")
    public UserDTO updateUser (@Argument UserDTO userDTO) {
        return _userService.update(userDTO);
    }

    //GraphQL Mutation to deactivate a user
    @MutationMapping
    @Secured("USER")
    public String desactivateUser () {
        UserDTO userDesactivated = _userService.deactivate();
        return "User with email: ".concat(userDesactivated.email()).concat(" has been desactivated.");
    }
}
