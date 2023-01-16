package com.app.bookstore.model.dto.userDTO;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDetailsDTO {

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = " email format is wrong")
    private String login;

    @Size(min = 1, max = 15, message = " name must be between 1 and 15 length")
    private String name;

    @Size(min = 1, max = 15, message = " surname must be between 1 and 15 length")
    private String surname;

    @Size(min = 9, max = 9, message = " phone must be 9 numbers length")
    private String phoneNumber;

}
