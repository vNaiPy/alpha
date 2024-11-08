package com.naipy.alpha.modules.user.service;

import com.naipy.alpha.modules.user.controllers.AddressInput;
import com.naipy.alpha.modules.user.controllers.UserController;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.*;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.service.UserAddressService;
import com.naipy.alpha.modules.utils.ServiceUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService extends ServiceUtils {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAddressService userAddressService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserAddressService userAddressService, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAddressService = userAddressService;
        this.authenticationService = authenticationService;
    }

    public AuthenticationResponse addNewUser (RegisterRequest request) {
        User user = User.builder()
                .id(ServiceUtils.generateUUID())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .build();
        return authenticationService.authenticateAfterInsertingNewUser(userRepository.save(user));
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    public UserAddress addAddressToUser (AddressInput addressInput) {
        return userAddressService.addAddressToUser(addressInput);
    }

    public List<UserDTO> findAll () {
        return userRepository.findAll()
                .stream().map(UserDTO::createUserDTO)
                .toList();
    }

    public UserDTO findById (String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) throw new ResourceNotFoundException(id);
        return UserDTO.createUserDTO(userOptional.get());
    }

    public void delete (String id) {
        try {
            userRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public User update (String id, User user) {
        try {
            User entity = userRepository.getReferenceById(id);
            updateData(user, entity);
            return userRepository.save(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

    }

    private void updateData(User user, User entity) {
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
    }

    public String encoder (String password) {return passwordEncoder.encode(password);}
}
