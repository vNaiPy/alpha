package com.naipy.alpha.config;

import com.naipy.alpha.modules.address.models.Address;
import com.naipy.alpha.modules.address.repository.AddressRepository;
import com.naipy.alpha.modules.category.model.Category;
import com.naipy.alpha.modules.product.enums.ProductStatus;
import com.naipy.alpha.modules.product.model.Product;
import com.naipy.alpha.modules.product.repository.ProductRepository;
import com.naipy.alpha.modules.store.enums.StoreStatus;
import com.naipy.alpha.modules.store.models.Store;
import com.naipy.alpha.modules.store.repository.StoreRepository;
import com.naipy.alpha.modules.token.TokenRepository;
import com.naipy.alpha.modules.user.enums.UserStatus;
import com.naipy.alpha.modules.user.models.Role;
import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.user.repository.UserRepository;
import com.naipy.alpha.modules.category.repository.CategoryRepository;
import com.naipy.alpha.modules.user_address.enums.AddressUsageType;
import com.naipy.alpha.modules.user_address.models.UserAddress;
import com.naipy.alpha.modules.user_address.repository.UserAddressRepository;
import com.naipy.alpha.modules.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Configuration
@Profile("dev")
public class TestConfig extends ServiceUtils implements CommandLineRunner {

    private final UserRepository _userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AddressRepository _addressRepository;

    private final StoreRepository _storeRepository;

    private final TokenRepository _tokenRepository;

    private final UserAddressRepository _userAddressRepository;

    private final ProductRepository _productRepository;

    private final CategoryRepository _categoryRepository;

    @Autowired
    public TestConfig(UserRepository _userRepository, StoreRepository storeRepository, TokenRepository tokenRepository, ProductRepository productRepository, UserAddressRepository userAddressRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder, CategoryRepository _categoryRepository) {
        this._userRepository = _userRepository;
        this._storeRepository = storeRepository;
        this._tokenRepository = tokenRepository;
        this._addressRepository = addressRepository;
        this._productRepository = productRepository;
        this._userAddressRepository = userAddressRepository;
        this.passwordEncoder = passwordEncoder;
        this._categoryRepository = _categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        _productRepository.deleteAll();
        _storeRepository.deleteAll();
        _categoryRepository.deleteAll();
        _tokenRepository.deleteAll();
        _userAddressRepository.deleteAll();
        _addressRepository.deleteAll();
        _userRepository.deleteAll();

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

        Address address = Address.builder()
                .id(generateUUID())
                .street("Avenida Paulista")
                .neighborhood("Bela Vista")
                .zipcode("01310920")
                .latitude(-23.561769)
                .longitude(-46.654062)
                .city("São Paulo")
                .state("SP")
                .country("Brasil")
                .build();

        _addressRepository.save(address);

        Category category = Category.builder()
                .id(generateUUID())
                .name("Eletrônicos")
                .imgUrl("https://")
                .subname("Notebook")
                .build();

        Store store = Store.builder()
                .id(generateUUID())
                .name("Loja do Handrei")
                .logoUrl("https://")
                .bannerUrl("https://")
                .description("A melhor loja de eletrônicos")
                .createdAt(Instant.now())
                .storeStatus(StoreStatus.ACTIVE)
                .owner(admin)
                .build();

        _storeRepository.save(store);

        _categoryRepository.save(category);

        UserAddress userAddress = new UserAddress(admin, address, "Apartment 101", "100",-23.561833, -46.653912, AddressUsageType.BUSINESS);
        _userAddressRepository.save(userAddress);

        Product product = Product.builder()
                .id(generateUUID())
                .name("Notebook")
                .description("Notebook Gamer")
                .price(5000.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();


        Product product1 = Product.builder()
                .id(generateUUID())
                .name("Notebook")
                .description("Notebook Gamer")
                .price(5000.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product2 = Product.builder()
                .id(generateUUID())
                .name("Smartphone")
                .description("Smartphone Android")
                .price(2500.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product3 = Product.builder()
                .id(generateUUID())
                .name("Tablet")
                .description("Tablet 10 polegadas")
                .price(1800.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product4 = Product.builder()
                .id(generateUUID())
                .name("Monitor")
                .description("Monitor 4K")
                .price(1200.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product5 = Product.builder()
                .id(generateUUID())
                .name("Teclado Mecânico")
                .description("Teclado RGB")
                .price(350.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product6 = Product.builder()
                .id(generateUUID())
                .name("Mouse Gamer")
                .description("Mouse com DPI ajustável")
                .price(200.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product7 = Product.builder()
                .id(generateUUID())
                .name("Headset")
                .description("Headset com microfone")
                .price(400.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product8 = Product.builder()
                .id(generateUUID())
                .name("Webcam")
                .description("Webcam Full HD")
                .price(250.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product9 = Product.builder()
                .id(generateUUID())
                .name("Impressora")
                .description("Impressora multifuncional")
                .price(600.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product10 = Product.builder()
                .id(generateUUID())
                .name("Roteador")
                .description("Roteador Wi-Fi 6")
                .price(350.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product11 = Product.builder()
                .id(generateUUID())
                .name("HD Externo")
                .description("HD Externo 1TB")
                .price(400.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product12 = Product.builder()
                .id(generateUUID())
                .name("SSD")
                .description("SSD 512GB")
                .price(500.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product13 = Product.builder()
                .id(generateUUID())
                .name("Placa de Vídeo")
                .description("Placa de vídeo RTX")
                .price(3500.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product14 = Product.builder()
                .id(generateUUID())
                .name("Fonte ATX")
                .description("Fonte 650W")
                .price(300.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product15 = Product.builder()
                .id(generateUUID())
                .name("Gabinete")
                .description("Gabinete Gamer")
                .price(450.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        Product product16 = Product.builder()
                .id(generateUUID())
                .name("Cadeira Gamer")
                .description("Cadeira ergonômica")
                .price(900.00)
                .imgUrl("https://")
                .status(ProductStatus.ACTIVE)
                .owner(admin)
                .categories(Set.of(category))
                .build();

        _productRepository.saveAll(List.of(product, product1, product2, product3, product4, product5, product6, product7, product8, product9, product10, product11, product12, product13, product14, product15, product16));

    }
}
