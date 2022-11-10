package com.example.bookstore.controller;

import com.example.bookstore.model.dto.BookHeaderDTO.BookHeaderDetailsIdIgnoreDTO;
import com.example.bookstore.model.dto.OrderHeaderDTO;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody OrderHeaderDTO orderHeaderDTO){
        orderService.placeOrder(orderHeaderDTO);
        return ResponseEntity.ok(new MessageResponse("Order placed successfully"));
    }
}
