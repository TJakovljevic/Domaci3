package com.example.Domaci_3.repositories;

import com.example.Domaci_3.model.Permissions;
import com.example.Domaci_3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, Long> {



}
