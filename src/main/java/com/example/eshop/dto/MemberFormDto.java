package com.example.eshop.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MemberFormDto {

  @NotBlank(message = "Name is required")
  private String name;
  @NotBlank(message = "Email is required")
  @Email(message = "Email your email, and try again")
  private String email;
  @NotBlank(message = "Name is required")
  @Length(min = 4, max = 20, message = "Password length : 4 ~ 20")
  private String password;
  @NotBlank(message = "Address is required")
  private String address;
}
