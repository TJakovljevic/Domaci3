package com.example.Domaci_3.repositories;

import com.example.Domaci_3.model.Dish;
import com.example.Domaci_3.services.IService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends CrudRepository<Dish, Long> {



}
