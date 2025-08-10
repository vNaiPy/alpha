package com.naipy.alpha.modules.user.service;

import com.naipy.alpha.modules.user.controllers.AddressInput;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.*;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.exceptions.services.DatabaseException;
import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.service.UserAddressService;
import com.naipy.alpha.modules.utils.ServiceUtils;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends ServiceUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .phone(request.phone())
                .identityDocument(request.identityDocument())
                .password(passwordEncoder.encode(request.password()))
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

    public UserDTO findById (final String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) throw new ResourceNotFoundException(id);
        return UserDTO.createUserDTO(userOptional.get());
    }

    public UserDTO findByUsername (final String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            final String message = "Username not found: ".concat(username);
            logger.warn(message);
            throw new ResourceNotFoundException(message);
        }
        return UserDTO.createUserDTO(userOptional.get());
    }

    public List<UserDTO> findByUsernameContaining (final String username) {
        List<User> userList = userRepository.findByUsernameContainingIgnoreCase(username);
        if (userList.isEmpty()) {
            final String message = "Username not found: ".concat(username);
            logger.warn(message);
            throw new ResourceNotFoundException(message);
        }
        return userList.stream().map(UserDTO::createUserDTO).toList();
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

    public UserDTO update (UserDTO user) {
        try {
            User entity = userRepository.getReferenceById(getIdCurrentUser().getId());
            updateData(user, entity);
            return UserDTO.createUserDTO(userRepository.save(entity));
        }
        catch (EntityNotFoundException e) {
            logger.warn("There is no user to update with ID: ".concat(getIdCurrentUser().getId()));
            throw new ResourceNotFoundException(e.getMessage());
        }

    }

    public UserDTO deactivate() {
        try {
            User entity = userRepository.getReferenceById(getIdCurrentUser().getId());
            entity.setStatus(UserStatus.DESACTIVATED);
            return UserDTO.createUserDTO(userRepository.save(entity));
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    private void updateData(UserDTO userDTO, User entity) {
        entity.setName(userDTO.name());
        entity.setSurname(userDTO.surname());
        entity.setEmail(userDTO.email());
        entity.setPhone(userDTO.phone());
        entity.setStatus(userDTO.status());
        entity.setProfilePicture(userDTO.profilePicture());
    }
}
