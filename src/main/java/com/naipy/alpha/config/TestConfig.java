package com.naipy.alpha.config;

import com.naipy.alpha.modules.country.models.Country;
import com.naipy.alpha.modules.country.repository.CountryRepository;
import com.naipy.alpha.modules.state.models.State;
import com.naipy.alpha.modules.state.repository.StateRepository;
import com.naipy.alpha.modules.user_address.repository.UserAddressRepository;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.category.repository.CategoryRepository;
import com.naipy.alpha.modules.order.repository.OrderRepository;
import com.naipy.alpha.modules.order_item.repository.OrderItemRepository;
import com.naipy.alpha.modules.product.repository.ProductRepository;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private CountryRepository _countryRepository;

    @Autowired
    private StateRepository _stateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository _categoryRepository;

    @Override
    public void run(String... args) throws Exception {

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


        /*User admin = User.builder()
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

        Category cat1 = Category.builder().name("Computers").build();
        Category cat2 = Category.builder().name("Eletronics").build();
        Category cat3 = Category.builder().name("Books").build();
        _categoryRepository.saveAll(List.of(cat1, cat2, cat3));*/
    }
}
