package com.example.bookstore.repository;

import com.example.bookstore.model.entities.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long>, JpaSpecificationExecutor<OrderHeader> {
    boolean existsByOrderItems_BookHeader_BookHeaderIdAndUser_Login(int bookHeaderId, String login);

    boolean existsByOrderIdAndOrderStatus_StatusId(Long orderId, Integer statusId);


}
