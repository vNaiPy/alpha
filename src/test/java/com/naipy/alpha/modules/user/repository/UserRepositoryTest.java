package com.naipy.alpha.modules.user.repository;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.city.models.City;
import com.naipy.alpha.modules.city.repository.CityRepository;
import com.naipy.alpha.modules.country.models.Country;
import com.naipy.alpha.modules.country.repository.CountryRepository;
import com.naipy.alpha.modules.state.models.State;
import com.naipy.alpha.modules.state.repository.StateRepository;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.repository.UserAddressRepository;
import com.naipy.alpha.modules.utils.ServiceUtils;
import com.naipy.alpha.modules.zipcode.models.ZipCode;
import com.naipy.alpha.modules.zipcode.repository.ZipCodeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository _userRepository;

    @Autowired
    CountryRepository _countryRepository;

    @Autowired
    StateRepository _stateRepository;

    @Autowired
    CityRepository _cityRepository;

    @Autowired
    ZipCodeRepository _zipCodeRepository;

    @Autowired
    AddressRepository _addressRepository;

    @Autowired
    UserAddressRepository _userAddressRepository;

    @BeforeEach
    void setUp () {

        Country country = Country.builder()
                .id(ServiceUtils.generateUUID())
                .name("Brasil")
                .code("BR")
                .build();

        _countryRepository.save(country);

        State state = State.builder()
                .id(ServiceUtils.generateUUID())
                .name("São Paulo")
                .code("SP")
                .country(country)
                .build();
        _stateRepository.save(state);

        City city = City.builder()
                .id(ServiceUtils.generateUUID())
                .name("São Bernardo do Campo")
                .code("SBC")
                .state(state)
                .build();
        _cityRepository.save(city);

        ZipCode zipCode = ZipCode.builder()
                .id(ServiceUtils.generateUUID())
                .code("09635130")
                .build();
        _zipCodeRepository.save(zipCode);

        Address address = Address.builder()
                .id(ServiceUtils.generateUUID())
                .street("Rua Gasparini")
                .neighborhood("Rudge Ramos")
                .city(city)
                .zipCode(zipCode)
                .build();
        _addressRepository.save(address);

        User admin = User.builder()
                .id(ServiceUtils.generateUUID())
                .name("Handrei Morais")
                .email("handrei@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.ADMIN, Role.USER))
                .build();

        User user = User.builder()
                .id(ServiceUtils.generateUUID())
                .name("Bruna Meyer")
                .email("bruna@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .roles(List.of(Role.USER))
                .build();

        _userRepository.saveAll(List.of(admin, user));


        UserAddress userAddress = new UserAddress(user, address, "Próximo ao Coop", "130", BigDecimal.valueOf(-23.651076), BigDecimal.valueOf(-46.57465730000001), true, AddressUsageType.PERSONAL);
        _userAddressRepository.save(userAddress);
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