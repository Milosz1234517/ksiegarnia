package com.example.bookstore.payload.request;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequest {

  @NotBlank
  @Size(min = 3, max = 20)
  private String login;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  private Set<String> role;

  @NotBlank
  @Size(min = 1, max = 15)
  private String name;

  @NotBlank
  @Size(min = 1, max = 15)
  private String surname;

  @NotBlank
  @Size(min = 9, max = 9)
  private String phoneNumber;

}
