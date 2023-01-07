package com.example.bookstore.service;

import com.example.bookstore.exceptions.BadRequestException;
import com.example.bookstore.jwt.JwtUtils;
import com.example.bookstore.model.dto.basketDTO.BasketBookIdDTO;
import com.example.bookstore.model.dto.basketDTO.BasketDTO;
import com.example.bookstore.model.dto.basketDTO.BasketUpdateDTO;
import com.example.bookstore.model.entities.Basket;
import com.example.bookstore.model.entities.BookHeader;
import com.example.bookstore.model.entities.Users;
import com.example.bookstore.payload.response.BasketTotalResponse;
import com.example.bookstore.repository.BasketRepository;
import com.example.bookstore.repository.BookHeaderRepository;
import com.example.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final ModelMapper modelMapper;
    private final BasketRepository basketRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final BookHeaderRepository bookHeaderRepository;

    public BasketTotalResponse getBasket(HttpServletRequest request){
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        BasketTotalResponse basketTotalResponse = new BasketTotalResponse();
        List<BasketDTO> basketDTO = basketRepository
                .findByUser_Login(users.getLogin(), Sort.by(Sort.Direction.ASC, "itemId"))
                .stream()
                .map(item -> {
                    basketTotalResponse.setTotalPrice(
                            basketTotalResponse.getTotalPrice().add(item.getPrice())
                    );
                    return modelMapper.map(item, BasketDTO.class);
                })
                .toList();
        basketTotalResponse.setBasket(basketDTO);
        return basketTotalResponse;
    }

    public void addItemToBasket(BasketBookIdDTO basketBookIdDTO, HttpServletRequest request){
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        Basket basket = new Basket();

        if(basketRepository.findByBookHeader_BookHeaderIdAndUser_Login(basketBookIdDTO.getBookHeaderId(), user.getLogin()).isPresent())
            throw new BadRequestException("Item already exist in basket, update quantity");

        BookHeader bookHeader = bookHeaderRepository.findByBookHeaderId(basketBookIdDTO.getBookHeaderId())
                .orElseThrow(()->new BadRequestException("Book not Found"));

        if(bookHeader.getQuantity() < 1)
            throw new BadRequestException("Item not enough quantity");
        basket.setBookHeader(bookHeader);
        basket.setUser(user);
        basket.setQuantity(1);
        basket.setPrice(bookHeader.getPrice());
        basketRepository.save(basket);
    }

    public BasketTotalResponse updateBasketItem(BasketUpdateDTO basketUpdateDTO, HttpServletRequest request){
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        Basket basket = basketRepository.findByBookHeader_BookHeaderIdAndUser_Login(basketUpdateDTO.getBookHeader().getBookHeaderId(), user.getLogin())
                .orElseThrow(()->new BadRequestException("Item not exist in basket"));

        BookHeader bookHeader = bookHeaderRepository.findByBookHeaderId(basketUpdateDTO.getBookHeader().getBookHeaderId())
                .orElseThrow(()->new BadRequestException("Book not Found"));

        if(bookHeader.getQuantity() < basketUpdateDTO.getQuantity())
            throw new BadRequestException("Item not enough quantity");

        basket.setQuantity(basketUpdateDTO.getQuantity());
        if(basket.getQuantity() < 1)
            deleteItemFromBasket(basket.getItemId());
        else {
            try {
                basket.setPrice(bookHeader.getPrice().multiply(BigDecimal.valueOf(basket.getQuantity())));
                basketRepository.save(basket);
            }catch (Exception ignore){}
        }
        return getBasket(request);
    }

    public void deleteItemFromBasket(Integer itemId){
        basketRepository.delete(basketRepository.findById(itemId)
                .orElseThrow(()-> new BadRequestException("Item not exist")));
    }

    public void clearBasket(HttpServletRequest request){
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow();
        basketRepository.deleteByUser(user);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
