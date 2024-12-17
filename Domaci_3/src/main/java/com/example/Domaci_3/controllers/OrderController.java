package com.example.Domaci_3.controllers;

import com.example.Domaci_3.dto.OrderDto;
import com.example.Domaci_3.dto.ScheduleOrderDto;
import com.example.Domaci_3.dto.SearchOrderDto;
import com.example.Domaci_3.model.Dish;
import com.example.Domaci_3.model.ErrorMessage;
import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.Status;
import com.example.Domaci_3.repositories.DishRepository;
import com.example.Domaci_3.repositories.ErrorRepository;
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
import java.util.concurrent.CompletableFuture;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final DishRepository dishRepository;
    private final ErrorRepository errorRepository;


    public OrderController(OrderService orderService, DishRepository dishRepository, ErrorRepository errorRepository){
        this.orderService = orderService;
        this.dishRepository = dishRepository;
        this.errorRepository = errorRepository;
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

        long count = this.orderService.countOrders(List.of(Status.PREPARING, Status.IN_DELIVERY));
        System.out.println("Count: " + count);
        Order order = new Order();
        if (count >= 1) {



            order.setStatus(Status.CANCELED);
            order.setCreatedBy(orderDto.getCreatedBy());
            order.setActive(false);
            order.setCreatedAt(LocalDateTime.now());
            List<Dish> dishes = this.dishRepository.findAllById(orderDto.getDishes());
            order.setDishes(dishes);


            Order savedOrder = this.orderService.save(order);

            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setOrderEntity(savedOrder);
            errorMessage.setTimestamp(LocalDateTime.now());
            errorMessage.setMessageDescription("Maximum concurrent orders exceeded");
            errorMessage.setStatus(Status.ORDERED);
            this.errorRepository.save(errorMessage);



            return null;
        }

        order.setStatus(Status.ORDERED);
        order.setCreatedBy(orderDto.getCreatedBy());
        order.setActive(orderDto.isActive());
        order.setCreatedAt(LocalDateTime.now());
        List<Dish> dishes = this.dishRepository.findAllById(orderDto.getDishes());
        order.setDishes(dishes);

        return this.orderService.save(order);
    }


    @PostMapping(value="/schedule", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> scheduleOrder(@RequestBody ScheduleOrderDto scheduleOrderDto){
        CompletableFuture<Order> order = this.orderService.scheduleOrder(scheduleOrderDto);
        if(order!=null)
            ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
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
