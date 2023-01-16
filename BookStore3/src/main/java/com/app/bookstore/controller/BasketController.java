package com.app.bookstore.controller;

import com.app.bookstore.model.dto.basketDTO.BasketBookIdDTO;
import com.app.bookstore.model.dto.basketDTO.BasketUpdateDTO;
import com.app.bookstore.payload.response.BasketTotalResponse;
import com.app.bookstore.payload.response.MessageResponse;
import com.app.bookstore.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/getBasket")
    @PreAuthorize("hasRole('USER')")
    public BasketTotalResponse getBasket(HttpServletRequest request) {
        return basketService.getBasket(request);
    }

    @PostMapping("/addItemToBasket")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addItemToBasket(@Valid @RequestBody BasketBookIdDTO basketBookIdDTO, HttpServletRequest request){
        basketService.addItemToBasket(basketBookIdDTO, request);
        return ResponseEntity.ok(new MessageResponse("Item added successfully"));
    }

    @PutMapping("/updateItem")
    @PreAuthorize("hasRole('USER')")
    public BasketTotalResponse updateItem(@Valid @RequestBody BasketUpdateDTO basketUpdateDTO, HttpServletRequest request){
        return basketService.updateBasketItem(basketUpdateDTO, request);
    }

    @DeleteMapping("/deleteItemFromBasket")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteItemFromBasket(@RequestParam Integer itemId){
        basketService.deleteItemFromBasket(itemId);
        return ResponseEntity.ok(new MessageResponse("Item deleted successfully"));
    }

    @DeleteMapping("/deleteAllItemsFromBasket")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteAllItemsFromBasket(HttpServletRequest httpServletRequest){
        basketService.clearBasket(httpServletRequest);
        return ResponseEntity.ok(new MessageResponse("Items deleted successfully"));
    }

}
