package com.example.bookstore.payload.request;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequest {


  @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Wrong email")
  private String username;

  @Size(min = 6, max = 40, message = "Password must be between 6 and 40 length")
  private String password;

  private Set<String> role;

  @Size(min = 1, max = 15, message = "Name must be between 1 and 15 length")
  private String name;

  @Size(min = 1, max = 15, message = "Surname must be between 1 and 15 length")
  private String sname;

  @Size(min = 9, max = 9, message = "Phone must be 9 numbers length")
  private String phone;

}
