package com.example.Domaci_3.services;

import com.example.Domaci_3.model.Dish;
import com.example.Domaci_3.repositories.DishRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DishService implements IService<Dish, Long>{

   private DishRepository dishRepository;

   public DishService(@Qualifier("dishRepository")DishRepository dishRepository){
       this.dishRepository = dishRepository;
   }

    @Override
    public <S extends Dish> S save(S dish) {
       return this.dishRepository.save(dish);
    }

    @Override
    public Optional<Dish> findById(Long id) {
        return this.dishRepository.findById(id);
    }

    @Override
    public List<Dish> findAll() {
        return (List<Dish>) this.dishRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        this.dishRepository.deleteById(id);
    }
}
