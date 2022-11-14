package com.example.bookstore.controller;

import com.example.bookstore.model.dto.UserDTO.UserDetailsDTO;
import com.example.bookstore.payload.response.MessageResponse;
import com.example.bookstore.service.UserService;
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
public class UserController {

    private final UserService userService;

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword( @RequestParam String oldPass, @RequestParam String newPass, HttpServletRequest request){
        userService.changePassword(oldPass, newPass, request);
        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }

    @PutMapping("/changeUserDetails")
    public ResponseEntity<?> changeUserDetails(@Valid @RequestBody UserDetailsDTO userDetailsDTO, HttpServletRequest request){
        userService.changeUserDetails(userDetailsDTO, request);
        return ResponseEntity.ok(new MessageResponse("Account details changed successfully"));
    }

    @GetMapping("/getUsersFilter")
    public List<UserDetailsDTO> getUsersFilter(
            String login,
            String name,
            String surname,
            @RequestParam Integer page
    ){
        return userService.getUsersFilter(login, name, surname, page);
    }

}
