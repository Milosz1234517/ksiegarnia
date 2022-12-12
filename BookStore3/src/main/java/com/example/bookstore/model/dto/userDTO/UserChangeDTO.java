package com.example.bookstore.model.dto.userDTO;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserChangeDTO {

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Wrong email")
    private String login;

    @Size(min = 1, max = 15, message = "Name must be between 1 and 15 length")
    private String name;

    @Size(min = 1, max = 15, message = "Surname must be between 1 and 15 length")
    private String surname;

    @Size(min = 9, max = 9, message = "Phone must be 9 numbers length")
    private String phoneNumber;

    private String token;

}
