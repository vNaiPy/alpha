package com.naipy.alpha.modules.user.service;

import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.models.UserDTO;
import com.naipy.alpha.modules.user.repository.UserRepository;

import com.naipy.alpha.modules.utils.ServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    @Autowired
    UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmail() {

        String id = ServiceUtils.generateUUID();
        User user = User.builder()
                .id(id)
                .name("Bruna Meyer")
                .email("bruna@mail.com")
                .phone("119999999999")
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> userMocked = userRepository.findById(id);
        if (userMocked.isEmpty()) throw new ResourceNotFoundException("User not found by ID: " + id);

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        System.out.println(UserDTO.createUserDTO(userRepository.findById(id).get()).toString());
    }
}
