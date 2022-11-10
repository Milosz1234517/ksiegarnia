package com.example.bookstore.service;

import com.example.bookstore.model.dto.OrderHeaderDTO;
import com.example.bookstore.model.dto.OrderItemDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.OrderHeader;
import com.example.bookstore.model.entities.OrderItems;
import com.example.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springfox.documentation.swagger2.mappers.ModelMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ModelMapper modelMapper;
    private final BookHeaderRepository bookHeaderRepository;

    public void placeOrder(OrderHeaderDTO order) {

        OrderHeader orderHeader = getOrderHeader(order);

        List<OrderItems> orderItems = order
                .getOrderItems()
                .stream()
                .map(orderItemDTO -> getOrderItems(orderHeader, orderItemDTO)).toList();

        orderHeader.setItemQuantity(orderItems.size());

        orderHeaderRepository.save(orderHeader);
        orderItemsRepository.saveAll(orderItems);

    }

    public void cancelOrder(){

    }

    private OrderHeader getOrderHeader(OrderHeaderDTO order) {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setUser(userRepository.findByLogin(order.getUser().getLogin()).orElseThrow());
        orderHeader.setOrderStatus(orderStatusRepository.findById(1).orElseThrow());
        orderHeader.setOrderDate(Date.valueOf(LocalDate.now()));
        orderHeader.setDescription(order.getDescription());
        orderHeader.setTotalPrice(BigDecimal.valueOf(0));
        return orderHeader;
    }

    private OrderItems getOrderItems(OrderHeader orderHeader, OrderItemDTO orderItemDTO) {
        BookHeader bookHeader = bookHeaderRepository
                .findByBookHeaderId(orderItemDTO.getBookHeader().getBookHeaderId()).orElseThrow();

        if(bookHeader.getQuantity() < orderItemDTO.getQuantity() || bookHeader.getQuantity() < 0)
            throw new RuntimeException();
        else
            bookHeader.setQuantity(bookHeader.getQuantity() - orderItemDTO.getQuantity());

        orderHeader.setTotalPrice(orderHeader.getTotalPrice().add(bookHeader.getPrice()));

        OrderItems ord = new OrderItems();
        ord.setBookHeader(bookHeader);
        ord.setPrice(bookHeader.getPrice());
        ord.setOrderHeader(orderHeader);
        ord.setQuantity(orderItemDTO.getQuantity());
        return ord;
    }

}
