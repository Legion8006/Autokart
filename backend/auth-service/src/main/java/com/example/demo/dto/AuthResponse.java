package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private Long id;
    private Long dealerId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;

    public AuthResponse(String token, Long id, String firstName, String lastName, String email, String role) {
        this.token = token;
        this.id = id;
        this.dealerId = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.status = "ACTIVE";
    }
}