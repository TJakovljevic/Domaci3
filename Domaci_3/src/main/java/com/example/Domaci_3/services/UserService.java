package com.example.Domaci_3.services;

import com.example.Domaci_3.model.Permissions;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService,IService<User, Long>  {

    private UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository){
        this.userRepository = userRepository;
    }


    //mozda umesto email da bude username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User myUser = this.userRepository.findByEmail(email);
        if(myUser == null){
            throw new UsernameNotFoundException("User with email " + email  + " not found!");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(), new ArrayList<>());

    }

    public List<Permissions> getUserPermissions(String email) {
        User user = userRepository.findByEmail(email);
        return user.getPermissions();
    }


    @Override
    public <S extends User> S save(S user) {
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long user_id) {
        return this.userRepository.findById(user_id);
    }
    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long user_id) {
       this.userRepository.deleteById(user_id);
    }
}
