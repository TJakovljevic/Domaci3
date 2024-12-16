package com.example.Domaci_3.controllers;

import com.example.Domaci_3.dto.OrderDto;
import com.example.Domaci_3.dto.SearchOrderDto;
import com.example.Domaci_3.model.Dish;
import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.Status;
import com.example.Domaci_3.repositories.DishRepository;
import com.example.Domaci_3.services.DishService;
import com.example.Domaci_3.services.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @GetMapping(value="/user/{email}")
    public List<Order> getUserOrders(@PathVariable String email){
        return this.orderService.getUserOrders(email);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Order> trackOrder(@PathVariable Long id){
        Optional<Order> optionalOrder = this.orderService.findById(id);
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping(value="/search", consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> searchOrder(@RequestBody SearchOrderDto searchOrderDto){
        return this.orderService.searchOrder(searchOrderDto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Order placeOrder(@RequestBody OrderDto orderDto) {
        Order order = new Order();
        order.setStatus(Status.ORDERED);
        order.setCreatedBy(orderDto.getCreatedBy());
        order.setActive(orderDto.isActive());
        order.setCreatedAt(LocalDateTime.now());
        List<Dish> dishes = this.dishRepository.findAllById(orderDto.getDishes());
        order.setDishes(dishes);

        return this.orderService.save(order);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id){
        Optional<Order> optionalOrder = this.orderService.findById(id);
        if(optionalOrder.isPresent()){
            Order order = optionalOrder.get();
            if(order.getStatus().equals(Status.ORDERED)) {
                order.setStatus(Status.CANCELED);
                order.setActive(false);
                this.orderService.save(order);
                return ResponseEntity.ok(order);
            }
        }
        return ResponseEntity.notFound().build();
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
