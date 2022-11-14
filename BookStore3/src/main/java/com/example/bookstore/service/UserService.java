package com.example.bookstore.service;


import com.example.bookstore.exceptions.BadRequestException;
import com.example.bookstore.jwt.JwtUtils;
import com.example.bookstore.model.dto.UserDTO.UserDetailsDTO;
import com.example.bookstore.model.entities.Users;
import com.example.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final ModelMapper modelMapper;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public void changePassword(String oldPass, String newPass, HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request)))
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), oldPass)).isAuthenticated()
        ) {
            user.setPassword(encoder.encode(newPass));
            userRepository.save(user);
        } else
            throw new BadRequestException("Old password incorrect");
    }

    public void changeUserDetails(UserDetailsDTO userDetailsDTO, HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request)))
                .orElseThrow(() -> new BadRequestException("User not found"));
        try {
            user.setLogin(userDetailsDTO.getLogin());
            user.setName(userDetailsDTO.getName());
            user.setSurname(userDetailsDTO.getSurname());
            user.setPhoneNumber(Integer.parseInt(userDetailsDTO.getPhoneNumber()));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Error: Wrong phone number");
        }
        userRepository.save(user);
    }

    public List<UserDetailsDTO> getUsersFilter(
            String login,
            String name,
            String surname,
            Integer page
    ) {
        return userRepository
                .findAll(
                        Specification
                                .where(name == null ? null : nameContains(name))
                                .and(surname == null ? null : surnameContains(surname))
                                .and(login == null ? null : loginContains(login)),
                        PageRequest.of(--page, 20)
                ).stream()
                .map(user -> modelMapper.map(user, UserDetailsDTO.class))
                .distinct()
                .toList();
    }

    private static Specification<Users> loginContains(String expression) {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder
                    .like(
                            builder.upper(
                                    root.get("login")
                            ),
                            contains(expression).toUpperCase()
                    );
        };
    }

    private static Specification<Users> nameContains(String expression) {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder
                    .like(
                            builder.upper(
                                    root.get("name")
                            ),
                            contains(expression).toUpperCase()
                    );
        };
    }

    private static Specification<Users> surnameContains(String expression) {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder
                    .like(
                            builder.upper(
                                    root.get("surname")
                            ),
                            contains(expression).toUpperCase()
                    );
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

}
