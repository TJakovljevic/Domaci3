package com.example.Domaci_3.repositories;

import com.example.Domaci_3.model.Dish;
import com.example.Domaci_3.model.ErrorMessage;
import com.example.Domaci_3.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ErrorRepository extends CrudRepository<ErrorMessage, Long>, JpaRepository<ErrorMessage, Long> {
    @Query("SELECT e FROM ErrorMessage e " +
            "WHERE (e.orderEntity.id IN :orders)")
    Page<ErrorMessage> findErrors(
                                  @Param("orders") List<Long> orders,
                                  Pageable pageable);




}
