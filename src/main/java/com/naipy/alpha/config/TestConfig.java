package com.naipy.alpha.config;

import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.category.repository.CategoryRepository;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;

@Configuration
@Profile("test")
public class TestConfig extends ServiceUtils implements CommandLineRunner {

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository _categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        User admin = User.builder()
                .id(generateUUID())
                .username("handrei.morais")
                .name("Handrei")
                .surname("Morais")
                .email("handrei@mail.com")
                .phone("119999999999")
                .identityDocument("123456789")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.ADMIN, Role.USER))
                .pictureUrl("https://")
                .createdAt(Instant.now())
                .build();
        _userRepository.save(admin);
        /*User user = User.builder()
                .name("Bruna Meyer")
                .email("bruna@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .build();
        _userRepository.saveAll(List.of(admin, user));

        Category cat1 = Category.builder().name("Computers").build();
        Category cat2 = Category.builder().name("Eletronics").build();
        Category cat3 = Category.builder().name("Books").build();
        _categoryRepository.saveAll(List.of(cat1, cat2, cat3));*/
    }
}
