package com.example.Domaci_3.services;

import com.example.Domaci_3.dto.ScheduleOrderDto;
import com.example.Domaci_3.dto.SearchOrderDto;
import com.example.Domaci_3.model.*;
import com.example.Domaci_3.repositories.DishRepository;
import com.example.Domaci_3.repositories.ErrorRepository;
import com.example.Domaci_3.repositories.OrderRepository;
import com.example.Domaci_3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService implements IService<Order, Long>{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ErrorRepository errorRepository;
    private final DishRepository dishRepository;
    private TaskScheduler taskScheduler;

    private LocalDateTime taskTime;

    private static final int MAX_ORDERS = 3;

    public OrderService(@Qualifier("orderRepository") OrderRepository orderRepository, @Qualifier("userRepository") UserRepository userRepository,
                        ErrorRepository errorRepository, DishRepository dishRepository, TaskScheduler taskScheduler){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.errorRepository = errorRepository;
        this.dishRepository = dishRepository;
        this.taskScheduler = taskScheduler;
    }

    public List<Order> getUserOrders(String email){
        User user = this.userRepository.findByEmail(email);
        return this.orderRepository.findByCreatedBy(user);
    }





    //NA frontu cu da prikazem id ako je admin ako nije nema searcha po adminu
    public List<Order> searchOrder(SearchOrderDto searchOrderDto) {
        Long userId = searchOrderDto.getUserId();
        List<String> status = searchOrderDto.getStatus();
        LocalDateTime dateFrom = searchOrderDto.getDateFrom();
        LocalDateTime dateTo = searchOrderDto.getDateTo();

        if (status != null && status.isEmpty()) {
            status = null;
        }

        return orderRepository.searchOrders(userId, status, dateFrom, dateTo);


    }

    //Neka random deviacija od 5 sekundi
    private int getDeviation(int baseSeconds) {
        return ThreadLocalRandom.current().nextInt(-5, 6) + baseSeconds;
    }
//    @Scheduled(fixedRate = 5000)
//    @Transactional
//    public void processOrderStatus(){
//        //ORDERED -> PREPARING
//        List<Order> orderedOrders = this.orderRepository.findByStatus(Status.ORDERED);
//        for(Order order: orderedOrders){
//
//        }
//    }


//    @Scheduled(fixedRate = 1000) //2 sec
//    public void checkAndUpdateStatus(){
//
//        List<Order> orders = (List<Order>) this.orderRepository.findAll();
//        // Transition orders from ORDERED -> PREPARING
//        for (Order order : orders) {
//            if (!order.isActive()) {
//                continue;
//            }
//            if (order.getStatus().equals(Status.ORDERED) && order.getCreatedAt().plusSeconds(10).isBefore(LocalDateTime.now())) {
//                order.setStatus(Status.PREPARING);
//                orderRepository.save(order);
//            }
//        }
//
//        // Transition orders from PREPARING -> IN_DELIVERY
//        for (Order order : orders) {
//            if (!order.isActive()) {
//                continue;
//            }
//            if (order.getStatus().equals(Status.PREPARING) && order.getCreatedAt().plusSeconds(15).isBefore(LocalDateTime.now())) {
//                order.setStatus(Status.IN_DELIVERY);
//                orderRepository.save(order);
//            }
//        }
//
//        // Transition orders from IN_DELIVERY -> DELIVERED
//        for (Order order : orders) {
//            if (!order.isActive()) {
//                continue;
//            }
//            if (order.getStatus().equals(Status.IN_DELIVERY) && order.getCreatedAt().plusSeconds(20).isBefore(LocalDateTime.now())) {
//                order.setStatus(Status.DELIVERED);
//                orderRepository.save(order);
//            }
//        }
//
//    }


    @Scheduled(fixedRate = 5000) // Runs every 5 seconds
    public void updateOrderStates() {
        List<Order> orders = (List<Order>) orderRepository.findAll();

        for (Order order : orders) {
            processOrderAsync(order);
        }
    }
    @Async
    public CompletableFuture<Order> scheduleOrder(ScheduleOrderDto scheduleOrderDto){
        Order order = new Order();
        order.setId(scheduleOrderDto.getId());
        order.setStatus(Status.ORDERED);
        order.setCreatedBy(scheduleOrderDto.getCreatedBy());
        order.setActive(scheduleOrderDto.isActive());
        List<Dish> dishes = this.dishRepository.findAllById(scheduleOrderDto.getDishes());
        order.setDishes(dishes);

        LocalDateTime scheduledTime = scheduleOrderDto.getScheduledTime();
        String cronTime = cronConverter(scheduledTime);

        CronTrigger cronTrigger = new CronTrigger(cronTime);
        this.taskScheduler.schedule(() -> {
            order.setCreatedAt(LocalDateTime.now()); // da bi krenula da se pravi od trenutka kada je stigla na red
            System.out.println("Saving order...");
            this.orderRepository.save(order);
        }, cronTrigger);

        return CompletableFuture.completedFuture(order);

}

    @Async
    @Transactional
    public void processOrderAsync(Order order) {
        switch (order.getStatus()) {
            case ORDERED:
                if (order.getCreatedAt().plusSeconds(10).isBefore(LocalDateTime.now())) {
                    order.setStatus(Status.PREPARING);
                    orderRepository.save(order);
                    System.out.println("Order " + order.getId() + " is now PREPARING");
                }
                break;
            case PREPARING:
                if (order.getCreatedAt().plusSeconds(25).isBefore(LocalDateTime.now())) { // 10s + 15s
                    order.setStatus(Status.IN_DELIVERY);
                    orderRepository.save(order);
                    System.out.println("Order " + order.getId() + " is now IN_DELIVERY");
                }
                break;
            case IN_DELIVERY:
                if (order.getCreatedAt().plusSeconds(45).isBefore(LocalDateTime.now())) { // 10s + 15s + 20s
                    order.setStatus(Status.DELIVERED);;
                    orderRepository.save(order);
                    System.out.println("Order " + order.getId() + " is now DELIVERED");
                }
                break;
        }
    }

    public static String cronConverter(LocalDateTime dateTime) {
        int second = dateTime.getSecond();  // Seconds (0-59)
        int minute = dateTime.getMinute();  // Minutes (0-59)
        int hour = dateTime.getHour();      // Hour (0-23)
        int dayOfMonth = dateTime.getDayOfMonth();  // Day of Month (1-31)
        int month = dateTime.getMonthValue();       // Month (1-12)
        int year = dateTime.getYear();     // Year

        return String.format("%d %d %d %d %d ?",
                second, minute, hour, dayOfMonth, month);
    }




    public long countOrders(List<Status> status){
        return this.orderRepository.countByStatusIn(status);
    }

    @Override
    public <S extends Order> S save(S order) {
        return this.orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return this.orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return (List<Order>) this.orderRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        this.orderRepository.deleteById(id);
    }

    public int getMaxOrders(){
        return MAX_ORDERS;
    }




}
