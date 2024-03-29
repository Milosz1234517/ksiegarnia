package com.app.bookstore.payload.request;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequest {


  @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = " wrong email")
  @Size(max = 255, message = " login too long")
  private String username;

  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",
  message = " password have incorrect format")
  @Size(max = 255, message = " password too long")
  private String password;

  @Size(min = 1, max = 15, message = " name must be between 1 and 15 length")
  private String name;

  @Size(min = 1, max = 15, message = " surname must be between 1 and 15 length")
  private String sname;

  @Min(value = 100000000, message = " phone must be 9 numbers")
  @Max(value = 999999999, message = " phone must be 9 numbers")
  @NotNull(message = " phone must be 9 numbers")
  private int phone;

}
