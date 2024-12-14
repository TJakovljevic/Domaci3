package com.example.Domaci_3.repositories;

import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    public List<Order> findByCreatedBy(User createdBy);
}
