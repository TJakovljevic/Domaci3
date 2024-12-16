package com.example.Domaci_3.services;

import com.example.Domaci_3.dto.SearchOrderDto;
import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.Status;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.repositories.OrderRepository;
import com.example.Domaci_3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService implements IService<Order, Long>{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(@Qualifier("orderRepository") OrderRepository orderRepository, @Qualifier("userRepository") UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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




}
