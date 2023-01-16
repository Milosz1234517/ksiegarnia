package com.app.bookstore.service;

import com.app.bookstore.model.dto.basketDTO.BasketBookIdDTO;
import com.app.bookstore.model.dto.basketDTO.BasketDTO;
import com.app.bookstore.model.dto.basketDTO.BasketUpdateDTO;
import com.app.bookstore.model.entities.Basket;
import com.app.bookstore.payload.response.BasketTotalResponse;
import com.app.bookstore.repository.BookHeaderRepository;
import com.app.bookstore.repository.UserRepository;
import com.app.bookstore.exceptions.BadRequestException;
import com.app.bookstore.jwt.AuthTokenFilter;
import com.app.bookstore.jwt.JwtUtils;
import com.app.bookstore.model.entities.BookHeader;
import com.app.bookstore.model.entities.Users;
import com.app.bookstore.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public BasketTotalResponse getBasket(HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request))).orElseThrow();
        BasketTotalResponse basketTotalResponse = new BasketTotalResponse();
        List<BasketDTO> basketDTO = basketRepository
                .findByUser_Login(users.getLogin(), Sort.by(Sort.Direction.ASC, "itemId"))
                .stream()
                .map(item -> {
                    item.setPrice(item.getBookHeader()
                            .getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                    basketTotalResponse.setTotalPrice(
                            basketTotalResponse.getTotalPrice().add(item.getPrice())
                    );
                    return modelMapper.map(item, BasketDTO.class);
                })
                .toList();
        basketTotalResponse.setBasket(basketDTO);
        return basketTotalResponse;
    }

    public void addItemToBasket(BasketBookIdDTO basketBookIdDTO, HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request))).orElseThrow();
        Basket basket = new Basket();

        if (basketRepository.findByBookHeader_BookHeaderIdAndUser_Login(basketBookIdDTO.getBookHeaderId(), user.getLogin()).isPresent())
            throw new BadRequestException("Item already exist in basket, update quantity");

        BookHeader bookHeader = bookHeaderRepository.findByBookHeaderId(basketBookIdDTO.getBookHeaderId())
                .orElseThrow(() -> new BadRequestException("Book not Found"));

        if (bookHeader.getQuantity() < 1)
            throw new BadRequestException("Item not enough quantity");
        basket.setBookHeader(bookHeader);
        basket.setUser(user);
        basket.setQuantity(1);
        basket.setPrice(bookHeader.getPrice());
        basketRepository.save(basket);
    }

    public BasketTotalResponse updateBasketItem(BasketUpdateDTO basketUpdateDTO, HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request))).orElseThrow();
        Basket basket = basketRepository.findByBookHeader_BookHeaderIdAndUser_Login(basketUpdateDTO.getBookHeader().getBookHeaderId(), user.getLogin())
                .orElseThrow(() -> new BadRequestException("Item not exist in basket"));

        BookHeader bookHeader = bookHeaderRepository.findByBookHeaderId(basketUpdateDTO.getBookHeader().getBookHeaderId())
                .orElseThrow(() -> new BadRequestException("Book not Found"));

        if (bookHeader.getQuantity() < basketUpdateDTO.getQuantity())
            throw new BadRequestException("Item not enough quantity");

        basket.setQuantity(basketUpdateDTO.getQuantity());
        if (basket.getQuantity() < 1)
            deleteItemFromBasket(basket.getItemId());
        else {
            basket.setPrice(bookHeader.getPrice().multiply(BigDecimal.valueOf(basket.getQuantity())));
            basketRepository.save(basket);
        }
        return getBasket(request);
    }

    public void deleteItemFromBasket(Integer itemId) {
        basketRepository.delete(basketRepository.findById(itemId)
                .orElseThrow(() -> new BadRequestException("Item not exist")));
    }

    public void clearBasket(HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(AuthTokenFilter.parseJwt(request))).orElseThrow();
        basketRepository.deleteByUser(user);
    }
}
