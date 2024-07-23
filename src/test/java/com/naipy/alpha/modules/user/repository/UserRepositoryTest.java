package com.naipy.alpha.modules.user.repository;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.repository.UserAddressRepository;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    UserRepository _userRepository;

    @Autowired
    AddressRepository _addressRepository;

    @Autowired
    UserAddressRepository _userAddressRepository;

    @BeforeEach
    void setUp () {

        Address address = Address.builder()
                .id(ServiceUtils.generateUUID())
                .street("Rua Gasparini")
                .neighborhood("Rudge Ramos")
                .city("São Bernardo do Campo")
                .state("São Paulo")
                .country("Brasil")
                .longitude(BigDecimal.valueOf(-46.57409550000001))
                .latitude(BigDecimal.valueOf(-23.6509129))
                .zipcode("09635130")
                .build();
        address = _addressRepository.save(address);

        User admin = User.builder()
                .id(ServiceUtils.generateUUID())
                .name("Handrei")
                .surname("Morais de Souza")
                .email("handrei@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.ADMIN, Role.USER))
                .profilePicture("https://")
                .registeredSince(Instant.now())
                .lastUpdate(Instant.now())
                .build();

        User user = User.builder()
                .id(ServiceUtils.generateUUID())
                .name("Bruna")
                .surname("Meyer")
                .email("bruna@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .profilePicture("https://")
                .registeredSince(Instant.now())
                .lastUpdate(Instant.now())
                .build();

        _userRepository.saveAll(List.of(admin, user));

        UserAddress userAddress = new UserAddress(user, address, "Próximo ao Coop", "130", BigDecimal.valueOf(-23.651076), BigDecimal.valueOf(-46.57465730000001), true, AddressUsageType.PERSONAL);
        System.out.println(_userAddressRepository.save(userAddress));
        System.out.println(_userAddressRepository.findUserAddressByUserId(userAddress.getUser().getId()));
    }

    @Test
    @DisplayName("findByEmail happy way")
    void findByEmailCase1() {
        Optional<User> userOptional = _userRepository.findByEmail("bruna@mail.com");
        System.out.println(userOptional.orElse(null));
        assertTrue(userOptional.isPresent());
    }

    @Test
    @DisplayName("findByEmail bad way")
    void findByEmailCase2() {
        Optional<User> userOptional = _userRepository.findByEmail("bruna@mai.com");
        System.out.println(userOptional.orElse(null));
        assertFalse(_userRepository.findByEmail("bruna@mai.com").isPresent());
    }

    @Test
    @DisplayName("getUserAddress happy way")
    void getUserAddressCase1() {

        var userOptional = _userAddressRepository.findUserAddressByUserId(ServiceUtils.generateUUID());
        System.out.println(userOptional.toString());
    }
}