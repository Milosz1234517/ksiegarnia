package com.example.bookstore.service;

import com.example.bookstore.jwt.JwtUtils;
import com.example.bookstore.model.dto.OrderHeaderDTO.OrderHeaderDTO;
import com.example.bookstore.model.dto.OrderHeaderDTO.OrderHeaderDetailsDTO;
import com.example.bookstore.model.dto.OrderItemDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.OrderHeader;
import com.example.bookstore.model.entities.OrderItems;
import com.example.bookstore.model.entities.Users;
import com.example.bookstore.model.entities.role.ERole;
import com.example.bookstore.model.entities.role.Role;
import com.example.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ModelMapper modelMapper;
    private final BookHeaderRepository bookHeaderRepository;
    private final JwtUtils jwtUtils;

    public void placeOrder(OrderHeaderDTO order, HttpServletRequest request) {

        OrderHeader orderHeader = getOrderHeader(order, request);

        List<OrderItems> orderItems = order
                .getOrderItems()
                .stream()
                .map(orderItemDTO -> getOrderItems(orderHeader, orderItemDTO)).toList();

        orderHeaderRepository.save(orderHeader);
        orderItemsRepository.saveAll(orderItems);

    }

    public void cancelOrder(Long orderID){
        OrderHeader orderHeader = orderHeaderRepository.findById(orderID).orElseThrow();
        if(orderHeader.getOrderStatus().getStatusId() == 2 || orderHeader.getOrderStatus().getStatusId() == 3)
            throw new RuntimeException();
        orderHeader.setOrderStatus(orderStatusRepository.findById(2).orElseThrow());
        orderHeader.getOrderItems()
                .forEach(orderItem -> orderItem.getBookHeader()
                        .setQuantity(orderItem.getBookHeader().getQuantity() + orderItem.getQuantity()));
        orderHeaderRepository.save(orderHeader);
    }

    public void finalizeOrder(Long orderID){
        OrderHeader orderHeader = orderHeaderRepository.findById(orderID).orElseThrow();
        if(orderHeader.getOrderStatus().getStatusId() != 4)
            throw new RuntimeException();
        orderHeader.setRealizationDate(Date.valueOf(LocalDate.now()));
        orderHeader.setOrderStatus(orderStatusRepository.findById(3).orElseThrow());
        orderHeaderRepository.save(orderHeader);
    }

    public void bookOrder(Long orderID){
        OrderHeader orderHeader = orderHeaderRepository.findById(orderID).orElseThrow();
        if(orderHeader.getOrderStatus().getStatusId() != 1)
            throw new RuntimeException();
        orderHeader.setOrderStatus(orderStatusRepository.findById(4).orElseThrow());
        orderHeaderRepository.save(orderHeader);
    }

    public List<OrderHeaderDetailsDTO> getOrdersFilter(
            Integer orderId,
            Integer status,
            Date placedFrom,
            Date placedTo,
            Date finalizedFrom,
            Date finalizedTo,
            Integer page,
            HttpServletRequest request
    ){
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();

        return orderHeaderRepository
                .findAll(
                        Specification
                                .where(placedFrom == null ? null : placedFrom(placedFrom))
                                .and(placedTo == null ? null : placedTo(placedTo))
                                .and(finalizedFrom == null ? null : finalizedFrom(finalizedFrom))
                                .and(finalizedTo == null ? null : finalizedTo(finalizedTo))
                                .and(status == null ? null : orderStatus(status))
                                .and(orderId == null ? null : orderId(orderId))
                                .and(users.getRoles().contains(role) ? user(users.getUserId()) : null),
                        PageRequest.of(--page, 20)
                ).stream()
                .map(order -> modelMapper.map(order, OrderHeaderDetailsDTO.class))
                .distinct()
                .toList();
    }

    private static Specification<OrderHeader> user(Integer orderId) {
        return (root, query, builder) -> builder.equal(root.join("user").get("userId"), orderId);
    }
    private static Specification<OrderHeader> orderId(Integer orderId) {
        return (root, query, builder) -> builder.equal(root.get("orderId"), orderId);
    }
    private static Specification<OrderHeader> orderStatus(Integer status) {
        return (root, query, builder) -> builder.equal(root.join("orderStatus").get("statusId"), status);
    }

    private static Specification<OrderHeader> finalizedFrom(Date placedFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("realizationDate"), placedFrom);
    }

    private static Specification<OrderHeader> finalizedTo(Date placedTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("realizationDate"), placedTo);
    }

    private static Specification<OrderHeader> placedFrom(Date placedFrom) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("orderDate"), placedFrom);
    }

    private static Specification<OrderHeader> placedTo(Date placedTo) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("orderDate"), placedTo);
    }

    private OrderHeader getOrderHeader(OrderHeaderDTO order, HttpServletRequest request) {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setUser(userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow());
        orderHeader.setOrderStatus(orderStatusRepository.findById(1).orElseThrow());
        orderHeader.setOrderDate(Date.valueOf(LocalDate.now()));
        orderHeader.setDescription(order.getDescription());
        orderHeader.setTotalPrice(BigDecimal.valueOf(0));
        return orderHeader;
    }

    private OrderItems getOrderItems(OrderHeader orderHeader, OrderItemDTO orderItemDTO) {
        BookHeader bookHeader = bookHeaderRepository
                .findByBookHeaderId(orderItemDTO.getBookHeader().getBookHeaderId()).orElseThrow();

        if(bookHeader.getQuantity() < orderItemDTO.getQuantity() || bookHeader.getQuantity() < 0 || orderItemDTO.getQuantity() < 1)
            throw new RuntimeException();
        else
            bookHeader.setQuantity(bookHeader.getQuantity() - orderItemDTO.getQuantity());

        orderHeader.setTotalPrice(orderHeader.getTotalPrice()
                .add(bookHeader.getPrice().multiply(BigDecimal.valueOf(orderItemDTO.getQuantity()))));

        OrderItems ord = new OrderItems();
        ord.setBookHeader(bookHeader);
        ord.setPrice(bookHeader.getPrice());
        ord.setOrderHeader(orderHeader);
        ord.setQuantity(orderItemDTO.getQuantity());
        return ord;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
