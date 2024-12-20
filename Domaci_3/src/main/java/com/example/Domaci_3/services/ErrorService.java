package com.example.Domaci_3.services;

import com.example.Domaci_3.model.ErrorMessage;
import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.User;
import com.example.Domaci_3.repositories.ErrorRepository;
import com.example.Domaci_3.repositories.OrderRepository;
import com.example.Domaci_3.repositories.UserRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.core.Ordered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ErrorService implements IService<ErrorMessage, Long>{

    private final ErrorRepository errorRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public ErrorService(@Qualifier("errorRepository")ErrorRepository errorRepository, UserRepository userRepository,
                        OrderRepository orderRepository){
        this.errorRepository = errorRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }


    //Paginacija, posle dodaj Sort.by
    public Page<ErrorMessage> paginate(Integer page, Integer size, String email){
        User user = this.userRepository.findByEmail(email);
        List<Order> orders = this.orderRepository.findByCreatedBy(user);
        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .toList();

        boolean isAdmin = user.isAdmin();
        if(isAdmin){
            return this.errorRepository.findAll(PageRequest.of(page, size));
        }
        System.out.println("OrderIds: " + orderIds);
        return this.errorRepository.findErrors(orderIds,PageRequest.of(page, size));
    }


    @Override
    public <S extends ErrorMessage> S save(S error) {
        return this.errorRepository.save(error);
    }

    @Override
    public Optional<ErrorMessage> findById(Long id) {
        return this.errorRepository.findById(id);
    }

    @Override
    public List<ErrorMessage> findAll() {
        return this.errorRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        this.errorRepository.deleteById(id);
    }
}
