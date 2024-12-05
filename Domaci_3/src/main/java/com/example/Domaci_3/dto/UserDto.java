package com.example.Domaci_3.dto;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;
@Data
public class UserDto {

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private List<Long> permissions;

    @Override
    public String toString() {
        return "UserDto{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
