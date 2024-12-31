package com.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private String username;
    private String email;
    private String password;
    private String bio;
    // Getters and Setters
}

