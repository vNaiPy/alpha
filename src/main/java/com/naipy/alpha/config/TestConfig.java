package com.naipy.alpha.config;

import com.naipy.alpha.entities.*;
import com.naipy.alpha.entities.enums.OrderStatus;
import com.naipy.alpha.entities.enums.ProductStatus;
import com.naipy.alpha.entities.enums.Role;
import com.naipy.alpha.entities.enums.UserStatus;
import com.naipy.alpha.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderRepository _orderRepository;

    @Autowired
    private CategoryRepository _categoryRepository;

    @Autowired
    private ProductRepository _productRepository;

    @Autowired
    private OrderItemRepository _orderItemRepository;

    @Override
    public void run(String... args) throws Exception {

        User admin = User.builder()
                .name("Handrei Morais")
                .email("handrei@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .role(Role.ADMIN)
                .build();

        User user = User.builder()
                .name("Bruna Meyer")
                .email("bruna@mail.com")
                .phone("119999999999")
                .password(passwordEncoder.encode("123456"))
                .status(UserStatus.ACTIVE)
                .role(Role.USER)
                .build();
        _userRepository.saveAll(Arrays.asList(admin, user));

        Store store1 = Store.builder()
                .name("dreams")
                .logoUrl("logo-url")
                .bannerUrl("banner-url")
                .instant(Instant.parse("2019-06-20T21:53:07Z"))
                .owner(user)
                .build();

        user.setStore(store1);

        Store store2 = Store.builder()
                .name("lovecathy")
                .logoUrl("logo-url")
                .bannerUrl("banner-url")
                .instant(Instant.parse("2019-06-20T21:53:07Z"))
                .owner(admin)
                .build();
        admin.setStore(store2);
        _userRepository.saveAll(Arrays.asList(admin, user));

        Order o1 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.PAID, user);
        _orderRepository.save(o1);

        Category cat1 = Category.builder().name("Computers").build();
        Category cat2 = Category.builder().name("Eletronics").build();
        Category cat3 = Category.builder().name("Books").build();
        _categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3));

        Product p1 = new Product(null, "The Lord of the Rings",
                "Lorem ipsum dolor sit amet, consectetur.",
                90.5,
                "img-url",
                ProductStatus.ACTIVE,
                user);

        _productRepository.save(p1);

        p1.getCategories().add(cat3);
        _productRepository.save(p1);

        OrderItem oi1 = new OrderItem(o1, p1, 2, p1.getPrice());
        _orderItemRepository.save(oi1);

        Payment pay1 = new Payment(null, Instant.parse("2019-06-20T21:53:07Z"), o1);
        o1.setPayment(pay1);
        _orderRepository.save(o1);

        /*User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "123456");
        _userRepository.saveAll(Arrays.asList(u1, u2));

        Order o1 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.PAID, u1);
        Order o2 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), OrderStatus.SHIPPED, u2);
        Order o3 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), OrderStatus.WAITING_PAYMENT, u1);
        _orderRepository.saveAll(Arrays.asList(o1,o2,o3));

        Category cat1 = new Category(null, "Electronics");
        Category cat2 = new Category(null, "Books");
        Category cat3 = new Category(null, "Computers");
        _categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3));

        Product p1 = new Product(null, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        Product p2 = new Product(null, "Smart TV", "Nulla eu imperdiet purus. Maecenas ante.", 2190.0, "");
        Product p3 = new Product(null, "Macbook Pro", "Nam eleifend maximus tortor, at mollis.", 1250.0, "");
        Product p4 = new Product(null, "PC Gamer", "Donec aliquet odio ac rhoncus cursus.", 1200.0, "");
        Product p5 = new Product(null, "Rails for Dummies", "Cras fringilla convallis sem vel faucibus.", 100.99, "");
        _productRepository.saveAll(Arrays.asList(p1,p2,p3,p4,p5));

        p1.getCategories().add(cat2);
        p2.getCategories().add(cat1);
        p2.getCategories().add(cat3);
        p3.getCategories().add(cat3);
        p4.getCategories().add(cat3);
        p5.getCategories().add(cat2);
        _productRepository.saveAll(Arrays.asList(p1,p2,p3,p4,p5));

        OrderItem oi1 = new OrderItem(o1, p1, 2, p1.getPrice());
        OrderItem oi2 = new OrderItem(o1, p3, 1, p3.getPrice());
        OrderItem oi3 = new OrderItem(o2, p3, 2, p3.getPrice());
        OrderItem oi4 = new OrderItem(o3, p5, 2, p5.getPrice());
        _orderItemRepository.saveAll(Arrays.asList(oi1, oi2, oi3, oi4));

        Payment pay1 = new Payment(null, Instant.parse("2019-06-20T21:53:07Z"), o1);
        o1.setPayment(pay1);
        _orderRepository.save(o1);*/
    }
}
