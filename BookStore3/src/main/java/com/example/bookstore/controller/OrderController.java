package com.example.bookstore.controller;

import com.example.bookstore.model.dto.OrderHeaderDTO.OrderHeaderDTO;
import com.example.bookstore.model.dto.OrderHeaderDTO.OrderHeaderDetailsDTO;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
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
    public ResponseEntity<?> cancelOrder(@RequestParam Long orderId){
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Order canceled successfully"));
    }

    @PutMapping("/finalizeOrder")
    public ResponseEntity<?> finalizeOrder(@RequestParam Long orderId){
        orderService.finalizeOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Order finalized successfully"));
    }

    @PutMapping("/bookOrder")
    public ResponseEntity<?> bookOrder(@RequestParam Long orderId){
        orderService.bookOrder(orderId);
        return ResponseEntity.ok(new MessageResponse("Order booked successfully"));
    }

    @GetMapping("/getOrdersFilterUser")
    public List<OrderHeaderDetailsDTO> getOrdersFilterUser(
            Integer status,
            Date placedFrom,
            Date placedTo,
            Date finalizedFrom,
            Date finalizedTo,
            @RequestParam Integer page
    ){
        return orderService.getOrdersFilterUser(status, placedFrom, placedTo, finalizedFrom, finalizedTo, page);
    }
}
