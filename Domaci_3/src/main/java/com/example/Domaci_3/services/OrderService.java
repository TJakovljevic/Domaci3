package com.example.Domaci_3.services;

import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.repositories.OrderRepository;
import com.example.Domaci_3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
