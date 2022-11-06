package com.example.bookstore.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

  private String accessToken;
  private String type;
  private Long id;
  private String username;
  private List<String> roles;

}
