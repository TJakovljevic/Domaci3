package com.example.Domaci_3.bootstrap;

import com.example.Domaci_3.model.*;
import com.example.Domaci_3.repositories.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PermissionsRepository permissionsRepository;
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final ErrorRepository errorRepository;

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder, PermissionsRepository permissionsRepository,
                         DishRepository dishRepository, OrderRepository orderRepository, ErrorRepository errorRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionsRepository = permissionsRepository;
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
        this.errorRepository = errorRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();

        user1.setFirst_name("Teodor");
        user1.setLast_name("Jakovljevic");
//        user1.setPermissions(Set.of(Permissions.CAN_CREATE_USERS, Permissions.CAN_DELETE_USERS));
        user1.setEmail("123@gmail.com");
        user1.setPassword(this.passwordEncoder.encode("123"));
        user1.setAdmin(true);
        this.userRepository.save(user1);

        Permissions permissions = new Permissions();
        permissions.setName("can_read_users");

        Permissions permissions1 = new Permissions();
        permissions1.setName("can_update_users");

        Permissions permissions2 = new Permissions();
        permissions2.setName("can_delete_users");

        Permissions permissions3 = new Permissions();
        permissions3.setName("can_create_users");

        Permissions permissions4 = new Permissions();
        permissions4.setName("can_search_order");

        Permissions permissions5 = new Permissions();
        permissions5.setName("can_place_order");

        Permissions permissions6 = new Permissions();
        permissions6.setName("can_cancel_order");

        Permissions permissions7 = new Permissions();
        permissions7.setName("can_track_order");

        Permissions permissions8 = new Permissions();
        permissions8.setName("can_schedule_order");




        this.permissionsRepository.save(permissions3);
        this.permissionsRepository.save(permissions2);
        this.permissionsRepository.save(permissions);
        this.permissionsRepository.save(permissions1);
        this.permissionsRepository.save(permissions4);
        this.permissionsRepository.save(permissions5);
        this.permissionsRepository.save(permissions6);
        this.permissionsRepository.save(permissions7);
        this.permissionsRepository.save(permissions8);


        Dish dish1 = new Dish();
        dish1.setName("Burek");
        dish1.setDescription("Lepo");
        dish1.setPrice(100);

        Dish dish2 = new Dish();
        dish2.setName("Sarma");
        dish2.setDescription("Lepo");
        dish2.setPrice(100);
        this.dishRepository.save(dish1);
        this.dishRepository.save(dish2);

        Order order = new Order();
        order.setStatus(Status.ORDERED);
        order.setActive(true);
        order.setCreatedBy(user1);
        order.setCreatedAt(LocalDateTime.now());

        this.orderRepository.save(order);

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(Status.DELIVERED);
        errorMessage.setMessageDescription("GRESKA");
        errorMessage.setTimestamp(LocalDateTime.now());
        errorMessage.setOrder(order);
        this.errorRepository.save(errorMessage);



    }
}
