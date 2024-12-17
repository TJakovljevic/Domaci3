package com.example.Domaci_3.repositories;

import com.example.Domaci_3.model.Order;
import com.example.Domaci_3.model.Status;
import com.example.Domaci_3.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    public List<Order> findByCreatedBy(User createdBy);

    public int countByStatusIn(List<Status> status);
    public List<Order> findByStatus(Status status);

    @Query("SELECT o FROM Order o WHERE " +
            "(:status IS NULL OR  o.status IN :status) AND " +
            "(:dateFrom IS NOT NULL AND :dateTo IS NOT NULL AND o.createdAt >= :dateFrom AND o.createdAt <= :dateTo) AND " +
            "(:userId IS NULL OR o.createdBy.id = :userId)")
    public List<Order> searchOrders(Long userId, List<String> status, LocalDateTime dateFrom, LocalDateTime dateTo);

}
