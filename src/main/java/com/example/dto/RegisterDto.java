package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterDto {

    @NotNull(message = "Username cannot be null.")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "Username can only contain alphanumeric characters, dots (.) and underscores (_), and cannot contain spaces.")
    private String username;

    @NotNull(message = "Email cannot be null.")
    @Email(message = "Email should be valid and include @gmail.com")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be a Gmail address.")
    private String email;

    @Pattern(regexp = "^\\S.*$", message = "Password cannot be null or empty.")
    private String password;

    private String bio;
}

