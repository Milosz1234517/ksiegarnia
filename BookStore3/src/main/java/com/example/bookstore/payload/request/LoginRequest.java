package com.example.bookstore.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

	@NotBlank(message = "Bad credentials login cannot be empty!")
  	private String username;

	@NotBlank(message = "Bad credentials password cannot be empty!")
	private String password;

}
