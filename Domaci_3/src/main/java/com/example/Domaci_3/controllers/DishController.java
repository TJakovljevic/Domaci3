package com.example.Domaci_3.controllers;

import com.example.Domaci_3.model.Dish;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.services.DishService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/dish")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService){
        this.dishService = dishService;
    }

    @GetMapping
    public List<Dish> getAllDishes(){
        return this.dishService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Dish createDish(@RequestBody Dish dish){
        return this.dishService.save(dish);
    }


    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> updateDish(@PathVariable Long id, @RequestBody Dish dish){
        Optional<Dish> optionalDish = this.dishService.findById(id);
        if(optionalDish.isPresent()){
            Dish existingDish = optionalDish.get();
            existingDish.setName(dish.getName());
            existingDish.setDescription(dish.getDescription());
            existingDish.setPrice(dish.getPrice());


            Dish updatedDish = this.dishService.save(existingDish);
            return ResponseEntity.ok(updatedDish);
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable Long id){
        Optional<Dish> optionalDish = this.dishService.findById(id);
        if(optionalDish.isPresent()){
            this.dishService.deleteById(id);
            return ResponseEntity.ok().build();
        }


       return ResponseEntity.notFound().build();
    }


}
