package com.app.bookstore.repository;

import com.app.bookstore.model.entities.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long>, JpaSpecificationExecutor<OrderHeader> {
    boolean existsByOrderItems_BookHeader_BookHeaderIdAndUser_Login(int bookHeaderId, String login);
    boolean existsByUser_LoginAndOrderStatus_StatusId(String login, Integer statusId);




}
