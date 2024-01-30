package com.naipy.alpha.modules.user.service;

import com.naipy.alpha.modules.exceptions.services.ResourceNotFoundException;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.models.UserDTO;
import com.naipy.alpha.modules.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository _userRepository;

    @InjectMocks
    @Autowired
    UserService _userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmail() {
        User user = User.builder()
                .id(4L)
                .name("Bruna Meyer")
                .email("bruna@mail.com")
                .phone("119999999999")
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .build();

        Mockito.when(_userRepository.findById(4L)).thenReturn(Optional.of(user));

        Optional<User> userMocked = _userRepository.findById(4L);
        if (userMocked.isEmpty()) throw new ResourceNotFoundException(4L);

        Mockito.verify(_userRepository, Mockito.times(1)).findById(4L);
        System.out.println(UserDTO.createUserDTO(_userRepository.findById(4L).get()).toString());;
    }
}
