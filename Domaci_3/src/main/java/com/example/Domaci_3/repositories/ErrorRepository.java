package com.example.Domaci_3.repositories;

import com.example.Domaci_3.model.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ErrorRepository extends CrudRepository<ErrorMessage, Long>, JpaRepository<ErrorMessage, Long> {
}
