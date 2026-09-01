package com.Bigproject.ecommerce_springboot.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO (

    @NotBlank(message = "username is Required ")
    String username,

    @NotBlank(message = "email is Required")
    @Email
    String email,

    @NotBlank(message = "password is Required")
    String userpassword,

    String role,
    
	String status
)

{
    
}
