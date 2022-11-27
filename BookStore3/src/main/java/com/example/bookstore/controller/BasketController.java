package com.example.bookstore.controller;

import com.example.bookstore.model.dto.CategoryDTO.CategoryDTO;
import com.example.bookstore.model.dto.basketDTO.BasketBookIdOnlyDTO;
import com.example.bookstore.model.dto.basketDTO.BasketDTO;
import com.example.bookstore.model.dto.basketDTO.BasketUpdateDTO;
import com.example.bookstore.model.entities.Category;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookstore")
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/getBasket")
    public List<BasketDTO> getBasket(HttpServletRequest request) {
        return basketService.getBasket(request);
    }

    @PostMapping("/addItemToBasket")
    public ResponseEntity<?> addItemToBasket(@RequestBody BasketBookIdOnlyDTO basketBookIdOnlyDTO, HttpServletRequest request){
        basketService.addItemToBasket(basketBookIdOnlyDTO, request);
        return ResponseEntity.ok(new MessageResponse("Item added successfully"));
    }

    @PutMapping("/updateItem")
    public ResponseEntity<?> updateItem(@RequestBody BasketUpdateDTO basketUpdateDTO, HttpServletRequest request){
        basketService.updateBasketItem(basketUpdateDTO, request);
        return ResponseEntity.ok(new MessageResponse("Item updated successfully"));
    }

    @DeleteMapping("/deleteItemFromBasket")
    public ResponseEntity<?> deleteItemFromBasket(@RequestParam Integer itemId){
        basketService.deleteItemFromBasket(itemId);
        return ResponseEntity.ok(new MessageResponse("Item deleted successfully"));
    }

}
