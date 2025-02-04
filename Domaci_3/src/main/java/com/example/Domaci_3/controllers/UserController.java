package com.example.Domaci_3.controllers;

import com.example.Domaci_3.dto.UserDto;
import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.Permissions;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.repositories.PermissionsRepository;
import com.example.Domaci_3.services.OrderService;
import com.example.Domaci_3.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PermissionsRepository permissionsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, PermissionsRepository permissionsRepository){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.permissionsRepository = permissionsRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers(){
        System.out.println("GetALLUsers");
        return userService.findAll();
    }

    @GetMapping(value="/paginate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        System.out.println("GetALLUsers");
        return this.userService.allUsers(page, size);
    }


    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getSingleUser(@PathVariable Long id){
        return this.userService.findById(id);
    }

    @GetMapping(value = "/permissions/{email}", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Permissions> getUserPermissions(@PathVariable String email){

        return userService.getUserPermissions(email);
    }

    @GetMapping(value = "/admin/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean userAdmin(@PathVariable String email){
        return this.userService.isUserAdmin(email);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody UserDto userDto){
        System.out.println(userDto);
        User user = new User();
        user.setFirst_name(userDto.getFirst_name());
        user.setLast_name(userDto.getLast_name());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        List<Permissions> permissions = permissionsRepository.findAllById(userDto.getPermissions());
        user.setPermissions(permissions);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));



      return this.userService.save(user);

    }

//     @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
//        Optional<User> optionalUser = this.userService.findById(id);
//        System.out.println("UserDto: " + userDto);
//
//        if (optionalUser.isPresent()) {
//            User existingUser = optionalUser.get();
//
//            // Update user fields
//            existingUser.setFirst_name(userDto.getFirst_name());
//            existingUser.setLast_name(userDto.getLast_name());
//
//            // Check if email is being updated
//            boolean emailChanged = !existingUser.getEmail().equals(userDto.getEmail());
//            existingUser.setEmail(userDto.getEmail());
//
//            // Update permissions
//            List<Permissions> permissions = permissionsRepository.findAllById(userDto.getPermissions());
//            existingUser.setPermissions(permissions);
//
//            // Save the updated user
//            User updatedUser = this.userService.save(existingUser);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("user", updatedUser);
//
//            if (emailChanged) {
//                String newJwt = jwtUtil.generateToken(updatedUser.getEmail());
//                response.put("token", newJwt);
//
//                // Update Spring Security context
//                Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
//                Authentication newAuth = new UsernamePasswordAuthenticationToken(
//                        updatedUser,
//                        currentAuth.getCredentials(),
//                        currentAuth.getAuthorities()
//                );
//                SecurityContextHolder.getContext().setAuthentication(newAuth);
//            }
//
//            return ResponseEntity.ok(response);
//        }
//
//        return ResponseEntity.notFound().build();
//    }
    @PutMapping(value="/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        Optional<User> optionalUser = this.userService.findById(id);
        System.out.println("UserDto: " + userDto);
        //stavio sam da se email ne menja, promeni kasnije ako bas bude trebalo
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirst_name(userDto.getFirst_name());
            existingUser.setLast_name(userDto.getLast_name());
            existingUser.setEmail(userDto.getEmail());
            //sifra se ne menja preko edit-a
//            existingUser.setPassword(userDto.getPassword());


            List<Permissions> permissions = permissionsRepository.findAllById(userDto.getPermissions());
            existingUser.setPermissions(permissions);

//            System.out.println("Existing user: " + existingUser);
            User updatedUser = this.userService.save(existingUser);

            return ResponseEntity.ok(updatedUser);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Optional<User> optionalUser = this.userService.findById(id);
        if(optionalUser.isPresent()){
            this.userService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
