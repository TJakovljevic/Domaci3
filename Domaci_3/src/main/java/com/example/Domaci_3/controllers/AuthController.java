package com.example.Domaci_3.controllers;

import com.example.Domaci_3.model.Permissions;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.requests.LoginRequest;
import com.example.Domaci_3.responses.LoginResponse;
import com.example.Domaci_3.services.UserService;
import com.example.Domaci_3.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;




    public AuthController(AuthenticationConfiguration authenticationConfiguration, UserService userService, JwtUtil jwtUtil) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<Permissions> permissions = userService.getUserPermissions(userDetails.getUsername());
            String jwtToken = jwtUtil.generateToken(permissions, userDetails.getUsername());
            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }




}
