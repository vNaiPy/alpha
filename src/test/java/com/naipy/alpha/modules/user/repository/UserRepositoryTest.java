package com.naipy.alpha.modules.user.repository;

import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.models.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository _userRepository;

    @BeforeEach
    void setUp () {
        User admin = User.builder()
                .name("Handrei Morais")
                .email("handrei@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.ADMIN, Role.USER))
                .build();

        User user = User.builder()
                .name("Bruna Meyer")
                .email("bruna@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .build();

        _userRepository.saveAll(List.of(admin, user));
        System.out.println(_userRepository.findAll());
    }

    @Test
    @DisplayName("findByEmail happy way")
    void findByEmailCase1() {
        assertTrue(_userRepository.findByEmail("bruna@mail.com").isPresent());
    }

    @Test
    @DisplayName("findByEmail bad way")
    void findByEmailCase2() {
        assertFalse(_userRepository.findByEmail("bruna@mai.com").isPresent());
    }
}