package com.example.bookstore.controller;

import com.example.bookstore.model.dto.orderDTO.OrderHeaderDTO;
import com.example.bookstore.model.dto.orderDTO.OrderHeaderDetailsDTO;
import com.example.bookstore.model.dto.orderDTO.OrderStatusDTO;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/placeOrder")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderHeaderDTO orderHeaderDTO, HttpServletRequest request){
        orderService.placeOrder(orderHeaderDTO, request);
        return ResponseEntity.ok(new MessageResponse("Order placed successfully"));
    }

    @PutMapping("/cancelOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cancelOrder(@RequestParam Long orderId){
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Order canceled successfully"));
    }

    @PutMapping("/finalizeOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> finalizeOrder(@RequestParam Long orderId){
        orderService.finalizeOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Order finalized successfully"));
    }

    @PutMapping("/bookOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> bookOrder(@RequestParam Long orderId){
        orderService.bookOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Order booked successfully"));
    }

    @GetMapping("/getOrdersFilterAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderHeaderDetailsDTO> getOrdersFilterAdmin(
            Integer orderId,
            Integer status,
            String placedFrom,
            String placedTo,
            String finalizedFrom,
            String finalizedTo,
            @RequestParam Integer page,
            HttpServletRequest request
    ){
        return orderService.getOrdersFilter(orderId, status, placedFrom, placedTo, finalizedFrom, finalizedTo, page, request);
    }

    @GetMapping("/getStatuses")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderStatusDTO> getStatuses(){
        return orderService.getStatuses();
    }

    @GetMapping("/getOrdersFilterUser")
    @PreAuthorize("hasRole('USER')")
    public List<OrderHeaderDetailsDTO> getOrdersFilterUser(
            Integer status,
            String placedFrom,
            String placedTo,
            @RequestParam Integer page,
            HttpServletRequest request

    ){
        return orderService.getOrdersFilter(null, status, placedFrom, placedTo, null, null, page, request);
    }

    @GetMapping("/getOrdersFilterAdminCount")
    @PreAuthorize("hasRole('ADMIN')")
    public Long getOrdersFilterAdminCount(
            Integer orderId,
            Integer status,
            String placedFrom,
            String placedTo,
            String finalizedFrom,
            String finalizedTo,
            HttpServletRequest request

    ){
        return orderService.getOrdersFilterCount(orderId, status, placedFrom, placedTo, finalizedFrom, finalizedTo, request);
    }

    @GetMapping("/getOrdersFilterUserCount")
    @PreAuthorize("hasRole('USER')")
    public Long getOrdersFilterUserCount(
            Integer status,
            String placedFrom,
            String placedTo,
            HttpServletRequest request

    ){
        return orderService.getOrdersFilterCount(null, status, placedFrom, placedTo, null, null, request);
    }
}
