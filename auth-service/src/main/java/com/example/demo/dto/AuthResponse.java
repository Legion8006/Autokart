package com.example.demo.dto;

import com.example.demo.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private Long id;

    private String name;

    private String email;

    private Role role;
}