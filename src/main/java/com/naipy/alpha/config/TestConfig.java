package com.naipy.alpha.config;

import com.naipy.alpha.entities.Category;
import com.naipy.alpha.entities.Order;
import com.naipy.alpha.entities.User;
import com.naipy.alpha.entities.enums.OrderStatus;
import com.naipy.alpha.repositories.CategoryRepository;
import com.naipy.alpha.repositories.OrderRepository;
import com.naipy.alpha.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private OrderRepository _orderRepository;

    @Autowired
    private CategoryRepository _categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
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
    }
}
