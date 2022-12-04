package com.example.bookstore.repository;

import com.example.bookstore.model.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    boolean existsByOrderHeaders_OrderStatus_StatusIdAndOrderHeaders_OrderId(Integer statusId, Long orderId);

}
