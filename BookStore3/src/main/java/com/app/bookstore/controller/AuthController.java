package com.app.bookstore.controller;

import java.util.*;
import javax.validation.Valid;

import com.app.bookstore.jwt.JwtUtils;
import com.app.bookstore.model.entities.Users;
import com.app.bookstore.model.entities.role.ERole;
import com.app.bookstore.model.entities.role.Role;
import com.app.bookstore.payload.request.LoginRequest;
import com.app.bookstore.payload.request.SignupRequest;
import com.app.bookstore.payload.response.MessageResponse;
import com.app.bookstore.repository.RoleRepository;
import com.app.bookstore.repository.UserRepository;
import com.app.bookstore.service.userDetailsService.UserDetailsImpl;
import com.app.bookstore.exceptions.BadRequestException;
import com.app.bookstore.payload.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userDetails.getRoles().forEach(role -> {
            if(!Objects.equals(role.getName().toString(), "ROLE_USER")){
                throw new BadRequestException("Bad user credentials");
            }
        });
        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userDetails.getRoles().forEach(role -> {
            if(!Objects.equals(role.getName().toString(), "ROLE_ADMIN")){
                throw new BadRequestException("Bad admin credentials");
            }
        });
        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByLogin(signUpRequest.getUsername())) {
            throw new BadRequestException("Error: Username is already taken!");
        }

        Users user;
        try {
            user = new Users(
                    signUpRequest.getUsername(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getName(),
                    signUpRequest.getSname(),
                    signUpRequest.getPhone());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Error: Wrong phone number");
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new BadRequestException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
