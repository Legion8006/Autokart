package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String city;

    private String state;

    private String role;
}