package com.example.Domaci_3.services;

import com.example.Domaci_3.dto.ScheduleOrderDto;
import com.example.Domaci_3.dto.SearchOrderDto;
import com.example.Domaci_3.model.*;
import com.example.Domaci_3.repositories.DishRepository;
import com.example.Domaci_3.repositories.ErrorRepository;
import com.example.Domaci_3.repositories.OrderRepository;
import com.example.Domaci_3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final SimpMessagingTemplate messagingTemplate;
    private TaskScheduler taskScheduler;


    private static final int MAX_ORDERS = 3;


    //Dodace se messagingTemplate koji ce da slusa kod update ordera i ukoliko je doslo do change statusa slace update
    //sa novim statusom, na frontu mogu da odradim novi model koji ce hvatati order.id i order.Status, tu porudzbinu nadjem preko
    //neke funkcije sa id-jem i izmenim mu status


    public OrderService(@Qualifier("orderRepository") OrderRepository orderRepository, @Qualifier("userRepository") UserRepository userRepository,
                        ErrorRepository errorRepository, DishRepository dishRepository, TaskScheduler taskScheduler, SimpMessagingTemplate simpMessagingTemplate){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.errorRepository = errorRepository;
        this.dishRepository = dishRepository;
        this.taskScheduler = taskScheduler;
        this.messagingTemplate = simpMessagingTemplate;
    }

    public List<Order> getUserOrders(String email){
        User user = this.userRepository.findByEmail(email);
        return this.orderRepository.findByCreatedBy(user);
    }






    //NA frontu cu da prikazem id ako je admin ako nije nema searcha po adminu
//    public List<Order> searchOrder(SearchOrderDto searchOrderDto) {
//        System.out.println("Search body" + searchOrderDto);
//        Long userId = null;
//        if(!searchOrderDto.getEmail().isEmpty()) {
//            User user = this.userRepository.findByEmail(searchOrderDto.getEmail());
//            userId = user.getId();
//        }
//
//        List<String> status = searchOrderDto.getStatus();
//        LocalDateTime dateFrom = searchOrderDto.getDateFrom();
//        LocalDateTime dateTo = searchOrderDto.getDateTo();
//
//
//        if (status != null && status.isEmpty()) {
//            status = null;
//        }
//
//        System.out.println("User: " + userId + ", " + "status: " + status + ", " + "dateFrom: " + dateFrom + ", dateTo: " + dateTo);
//
//        return orderRepository.searchOrders(userId, status, dateFrom, dateTo);
//
//
//    }
    public Page<Order> searchOrder(Integer page, Integer size,SearchOrderDto searchOrderDto) {
        System.out.println("Search body" + searchOrderDto);
        Long userId = null;
        if(!searchOrderDto.getEmail().isEmpty()) {
            User user = this.userRepository.findByEmail(searchOrderDto.getEmail());
            userId = user.getId();
        }

        List<String> status = searchOrderDto.getStatus();
        LocalDateTime dateFrom = searchOrderDto.getDateFrom();
        LocalDateTime dateTo = searchOrderDto.getDateTo();


        if (status != null && status.isEmpty()) {
            status = null;
        }

        System.out.println("User: " + userId + ", " + "status: " + status + ", " + "dateFrom: " + dateFrom + ", dateTo: " + dateTo);

        return orderRepository.searchOrders(PageRequest.of(page, size, Sort.by("createdAt").descending()), userId, status, dateFrom, dateTo);


    }


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

        User user = this.userRepository.findByEmail(scheduleOrderDto.getCreatedBy().getEmail());
        order.setCreatedBy(user);

        order.setActive(scheduleOrderDto.isActive());
        List<Dish> dishes = this.dishRepository.findAllById(scheduleOrderDto.getDishes());
        order.setDishes(dishes);

        LocalDateTime scheduledTime = scheduleOrderDto.getScheduledTime();
        String cronTime = cronConverter(scheduledTime);

        CronTrigger cronTrigger = new CronTrigger(cronTime);
        this.taskScheduler.schedule(() -> {
            long count = this.countOrders(List.of(Status.PREPARING, Status.IN_DELIVERY));
            if(count >= 3) {
                order.setStatus(Status.CANCELED);
                order.setActive(false);
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setTimestamp(LocalDateTime.now());
                errorMessage.setMessageDescription("Maximum concurrent orders exceeded");
                errorMessage.setStatus(Status.ORDERED);
                Order savedOrder = this.orderRepository.save(order);
                errorMessage.setOrderEntity(savedOrder);
                this.errorRepository.save(errorMessage);
            }else {
                order.setCreatedAt(LocalDateTime.now());
                System.out.println("Saving order...");
                this.orderRepository.save(order);
            }
        }, cronTrigger);

        return CompletableFuture.completedFuture(order);

}

    @Async
    @Transactional
    public synchronized void processOrderAsync(Order order) {

        //ako menjam onda samo umesto ovog freshOrdera stavim ovaj order od gore

        Order freshOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));


        List<StatusUpdate> updates = new ArrayList<>();

        switch (freshOrder.getStatus()) {
            case ORDERED:
                if (freshOrder.getCreatedAt().plusSeconds(10).isBefore(LocalDateTime.now())) {
                    freshOrder.setStatus(Status.PREPARING);
                    orderRepository.save(freshOrder);
                    updates.add(new StatusUpdate(order.getId(), Status.PREPARING.name()));
                    System.out.println("Order " + freshOrder.getId() + " is now PREPARING");
                }
                break;
            case PREPARING:
                if (freshOrder.getCreatedAt().plusSeconds(25).isBefore(LocalDateTime.now())) { // 10s + 15s
                    freshOrder.setStatus(Status.IN_DELIVERY);
                    orderRepository.save(freshOrder);
                    updates.add(new StatusUpdate(order.getId(), Status.IN_DELIVERY.name()));
                    System.out.println("Order " + freshOrder.getId() + " is now IN_DELIVERY");
                }
                break;
            case IN_DELIVERY:
                if (freshOrder.getCreatedAt().plusSeconds(45).isBefore(LocalDateTime.now())) { // 10s + 15s + 20s
                    freshOrder.setStatus(Status.DELIVERED);
                    orderRepository.save(freshOrder);
                    updates.add(new StatusUpdate(order.getId(), Status.DELIVERED.name()));;
                    System.out.println("Order " + freshOrder.getId() + " is now DELIVERED");
                }
                break;
        }

        if (!updates.isEmpty()) {
            updates.forEach(update -> messagingTemplate.convertAndSend("/topic/orders", update));
            System.out.println("Updates");
        }
    }


    public static String cronConverter(LocalDateTime dateTime) {
        int second = dateTime.getSecond();  // Seconds (0-59)
        int minute = dateTime.getMinute();  // Minutes (0-59)
        int hour = dateTime.getHour();      // Hour (0-23)
        int dayOfMonth = dateTime.getDayOfMonth();  // Day of Month (1-31)
        int month = dateTime.getMonthValue();       // Month (1-12)

        return String.format("%d %d %d %d %d ?",
                second, minute, hour, dayOfMonth, month);
    }


    public long countOrders(List<Status> status){
        return this.orderRepository.countByStatusIn(status);
    }

    @Override
    public <S extends Order> S save(S order) {
        User user = this.userRepository.findByEmail(order.getCreatedBy().getEmail());
        order.setCreatedBy(user);
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
