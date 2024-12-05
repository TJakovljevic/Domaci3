package com.example.Domaci_3.bootstrap;

import com.example.Domaci_3.model.Permissions;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.repositories.PermissionsRepository;
import com.example.Domaci_3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PermissionsRepository permissionsRepository;

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder, PermissionsRepository permissionsRepository) {
         this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionsRepository = permissionsRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();

        user1.setFirst_name("Teodor");
        user1.setLast_name("Jakovljevic");
//        user1.setPermissions(Set.of(Permissions.CAN_CREATE_USERS, Permissions.CAN_DELETE_USERS));
        user1.setEmail("123@gmail.com");
        user1.setPassword(this.passwordEncoder.encode("123"));
        this.userRepository.save(user1);

        Permissions permissions = new Permissions();
        permissions.setName("can_read_users");

        Permissions permissions1 = new Permissions();
        permissions1.setName("can_update_users");

        Permissions permissions2 = new Permissions();
        permissions2.setName("can_delete_users");

        Permissions permissions3 = new Permissions();
        permissions3.setName("can_create_users");




        this.permissionsRepository.save(permissions3);
        this.permissionsRepository.save(permissions2);
        this.permissionsRepository.save(permissions);
        this.permissionsRepository.save(permissions1);




    }
}
