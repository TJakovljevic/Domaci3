package com.example.Domaci_3.controllers;

import com.example.Domaci_3.dto.OrderDto;
import com.example.Domaci_3.dto.ScheduleOrderDto;
import com.example.Domaci_3.dto.SearchOrderDto;
import com.example.Domaci_3.model.*;
import com.example.Domaci_3.repositories.DishRepository;
import com.example.Domaci_3.repositories.ErrorRepository;
import com.example.Domaci_3.services.DishService;
import com.example.Domaci_3.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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

//    @PostMapping(value="/search", consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<Order> searchOrder(@RequestBody SearchOrderDto searchOrderDto){
//        return this.orderService.searchOrder(searchOrderDto);
//    }
    @PostMapping(value="/search", consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Order> searchOrder(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestBody SearchOrderDto searchOrderDto){
        return this.orderService.searchOrder(page, size, searchOrderDto);
    }



    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDto orderDto) {
        Order order = new Order();
        //CIM vidim koji su ubijem ih na orderu
        long count = this.orderService.countOrders(List.of(Status.ORDERED, Status.PREPARING, Status.IN_DELIVERY));
        if(count >= 3) {
            order.setStatus(Status.CANCELED);
            order.setActive(false);
            order.setCreatedAt(LocalDateTime.now());
            order.setCreatedBy(orderDto.getCreatedBy());
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setTimestamp(LocalDateTime.now());
            errorMessage.setMessageDescription("Maximum concurrent orders exceeded");
            errorMessage.setStatus(Status.ORDERED);
            Order savedOrder = this.orderService.save(order);
            errorMessage.setOrderEntity(savedOrder);
            this.errorRepository.save(errorMessage);
        }else {
            order.setStatus(Status.ORDERED);
            order.setCreatedBy(orderDto.getCreatedBy());
            order.setActive(orderDto.isActive());
            order.setCreatedAt(LocalDateTime.now());
            List<Dish> dishes = this.dishRepository.findAllById(orderDto.getDishes());
            order.setDishes(dishes);
            this.orderService.save(order);
        }

        return ResponseEntity.ok().build();
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
