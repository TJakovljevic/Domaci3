package com.example.Domaci_3.controllers;

import com.example.Domaci_3.model.Permissions;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.repositories.PermissionsRepository;
import com.example.Domaci_3.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionsRepository permissionsRepository;

    public PermissionController(PermissionsRepository permissionsRepository){
        this.permissionsRepository = permissionsRepository;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Permissions> getAllUsers(){
        System.out.println("GetALLUsers");
        return permissionsRepository.findAll();
    }
}
