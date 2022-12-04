package com.example.bookstore.service;

import com.example.bookstore.exceptions.BadRequestException;
import com.example.bookstore.jwt.JwtUtils;
import com.example.bookstore.model.dto.OrderHeaderDTO.OrderHeaderDTO;
import com.example.bookstore.model.dto.OrderHeaderDTO.OrderHeaderDetailsDTO;
import com.example.bookstore.model.dto.OrderItemDTO;
import com.example.bookstore.model.dto.OrderStatusDTO;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.OrderHeader;
import com.example.bookstore.model.entities.OrderItems;
import com.example.bookstore.model.entities.Users;
import com.example.bookstore.model.entities.role.ERole;
import com.example.bookstore.model.entities.role.Role;
import com.example.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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

    public List<OrderStatusDTO> getStatuses(){
        return orderStatusRepository
                .findAll()
                .stream()
                .map(s -> modelMapper.map(s, OrderStatusDTO.class))
                .toList();
    }

    public void cancelOrder(Long orderID){
        OrderHeader orderHeader = orderHeaderRepository.findById(orderID)
                .orElseThrow(() -> new BadRequestException("Order not found"));

        if(orderHeader.getOrderStatus().getStatusId() == 2 || orderHeader.getOrderStatus().getStatusId() == 3)
            throw new BadRequestException("Order cannot be canceled");

        orderHeader.setOrderStatus(orderStatusRepository.findById(2)
                .orElseThrow(() -> new BadRequestException("Status not found")));
        orderHeader.getOrderItems()

                .forEach(orderItem -> orderItem.getBookHeader()
                        .setQuantity(orderItem.getBookHeader().getQuantity() + orderItem.getQuantity()));

        orderHeaderRepository.save(orderHeader);
    }

    public void finalizeOrder(Long orderID){
        OrderHeader orderHeader = orderHeaderRepository.findById(orderID)
                .orElseThrow(() -> new BadRequestException("Order not found"));

        if(orderHeader.getOrderStatus().getStatusId() != 4)
            throw new BadRequestException("Order cannot be finalized");

        orderHeader.setRealizationDate(new Timestamp(System.currentTimeMillis()));

        orderHeader.setOrderStatus(orderStatusRepository.findById(3)
                .orElseThrow(() -> new BadRequestException("Status not found")));

        orderHeaderRepository.save(orderHeader);
    }

    public void bookOrder(Long orderID){
        OrderHeader orderHeader = orderHeaderRepository.findById(orderID)
                .orElseThrow(() -> new BadRequestException("Order not found"));

        if(orderHeader.getOrderStatus().getStatusId() != 1)
            throw new BadRequestException("Order cannot be booked");

        orderHeader.setOrderStatus(orderStatusRepository.findById(4)
                .orElseThrow(() -> new BadRequestException("Status not found")));

        orderHeaderRepository.save(orderHeader);
    }

    public Long getOrdersFilterCount(
            Integer orderId,
            Integer status,
            String placedFrom,
            String placedTo,
            String finalizedFrom,
            String finalizedTo,
            HttpServletRequest request
    ){
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();

        return orderHeaderRepository
                .count(
                        getOrderHeaderSpecification(orderId, status, placedFrom, placedTo, finalizedFrom, finalizedTo, users, role)
                );
    }

    private static Specification<OrderHeader> getOrderHeaderSpecification(Integer orderId, Integer status, String placedFrom, String placedTo, String finalizedFrom, String finalizedTo, Users users, Role role) {

        return Specification
                .where(placedFrom == null || placedFrom.equals("") ? null : placedFrom(Date.valueOf(placedFrom)))
                .and(placedTo == null || placedTo.equals("")? null : placedTo(Date.valueOf(placedTo)))
                .and(finalizedFrom == null || finalizedFrom.equals("")? null : finalizedFrom(Date.valueOf(finalizedFrom)))
                .and(finalizedTo== null || finalizedTo.equals("") ? null : finalizedTo(Date.valueOf(finalizedTo)))
                .and(status == null ? null : orderStatus(status))
                .and(orderId == null ? null : orderId(orderId))
                .and(users.getRoles().contains(role) ? user(users.getUserId()) : null);
    }

    public List<OrderHeaderDetailsDTO> getOrdersFilter(
            Integer orderId,
            Integer status,
            String placedFrom,
            String placedTo,
            String finalizedFrom,
            String finalizedTo,
            Integer page,
            HttpServletRequest request
    ){
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();



        return orderHeaderRepository
                .findAll(
                        getOrderHeaderSpecification(orderId, status, placedFrom, placedTo, finalizedFrom, finalizedTo, users, role),
                        PageRequest.of(--page, 2, Sort.by("orderDate").descending())
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
        orderHeader.setUser(userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request)))
                .orElseThrow(() -> new BadRequestException("User not found")));
        orderHeader.setOrderStatus(orderStatusRepository.findById(1)
                .orElseThrow(() -> new BadRequestException("Status not found")));
        orderHeader.setOrderDate(new Timestamp(System.currentTimeMillis()));
        orderHeader.setDescription(order.getDescription());
        orderHeader.setTotalPrice(BigDecimal.valueOf(0));
        return orderHeader;
    }

    private OrderItems getOrderItems(OrderHeader orderHeader, OrderItemDTO orderItemDTO) {
        BookHeader bookHeader = bookHeaderRepository
                .findByBookHeaderId(orderItemDTO.getBookHeader().getBookHeaderId())
                .orElseThrow(() -> new BadRequestException("Book not found"));

        if(bookHeader.getQuantity() < orderItemDTO.getQuantity())
            throw new BadRequestException("Wrong quantity, not enough books in warehouse");
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
