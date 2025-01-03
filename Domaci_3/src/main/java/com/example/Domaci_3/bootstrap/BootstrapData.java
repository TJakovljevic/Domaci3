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





        //Meni -> Stepin Vajat
        Dish dish1 = new Dish();
        dish1.setName("Batak pileci 200g");
        dish1.setDescription("Lepo");
        dish1.setPrice(400);

        Dish dish2 = new Dish();
        dish2.setName("Belo meso 200g");
        dish2.setDescription("Lepo");
        dish2.setPrice(400);

        Dish dish3 = new Dish();
        dish3.setName("Stepina traka 230g");
        dish3.setDescription("Lepo");
        dish3.setPrice(450);

        Dish dish4 = new Dish();
        dish4.setName("Pljeskavica 150g");
        dish4.setDescription("Lepo");
        dish4.setPrice(300);

        Dish dish5 = new Dish();
        dish5.setName("Punjena pljeskavica 200g");
        dish5.setDescription("Lepo");
        dish5.setPrice(350);

        Dish dish6 = new Dish();
        dish6.setName("Pohovani kackavalj 180g");
        dish6.setDescription("Lepo");
        dish6.setPrice(330);

        Dish dish7 = new Dish();
        dish7.setName("Dimnjeni vrat 150g");
        dish7.setDescription("Lepo");
        dish7.setPrice(400);

        Dish dish8 = new Dish();
        dish8.setName("Gurmanska pljeskavica 200g");
        dish8.setDescription("Lepo");
        dish8.setPrice(350);

        Dish dish9 = new Dish();
        dish9.setName("Kobasica 200g");
        dish9.setDescription("Lepo");
        dish9.setPrice(330);

        Dish dish10 = new Dish();
        dish10.setName("Pomfrit 200g");
        dish10.setDescription("Lepo");
        dish10.setPrice(250);

        this.dishRepository.save(dish1);
        this.dishRepository.save(dish2);
        this.dishRepository.save(dish3);
        this.dishRepository.save(dish4);
        this.dishRepository.save(dish5);
        this.dishRepository.save(dish6);
        this.dishRepository.save(dish7);
        this.dishRepository.save(dish8);
        this.dishRepository.save(dish9);
        this.dishRepository.save(dish10);






    }
}
