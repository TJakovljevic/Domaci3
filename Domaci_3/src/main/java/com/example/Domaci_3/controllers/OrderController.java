package com.example.Domaci_3.controllers;

import com.example.Domaci_3.dto.OrderDto;
import com.example.Domaci_3.model.Dish;
import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.Status;
import com.example.Domaci_3.repositories.DishRepository;
import com.example.Domaci_3.services.DishService;
import com.example.Domaci_3.services.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final DishRepository dishRepository;

    public OrderController(OrderService orderService, DishRepository dishRepository){
        this.orderService = orderService;
        this.dishRepository = dishRepository;
    }


    @GetMapping
    public List<Order> getAllOrders(){
        return this.orderService.findAll();
    }

    @GetMapping(value="/{email}")
    public List<Order> getUserOrders(@PathVariable String email){
        return this.orderService.getUserOrders(email);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Order createOrder(@RequestBody OrderDto orderDto) {
        Order order = new Order();
        order.setStatus(Status.ORDERED);
        order.setCreatedBy(orderDto.getCreatedBy());
        order.setActive(orderDto.isActive());
        List<Dish> dishes = this.dishRepository.findAllById(orderDto.getDishes());
        order.setDishes(dishes);




        return this.orderService.save(order);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id){
        Optional<Order> optionalOrder = this.orderService.findById(id);
        if(optionalOrder.isPresent()){
            this.orderService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
