package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "JWT authentication successful";
    }

    @GetMapping("/buyer")
    public String buyerEndpoint() {
        return "Buyer access successful";
    }

    @GetMapping("/dealer")
    public String dealerEndpoint() {
        return "Dealer access successful";
    }

    @GetMapping("/bank")
    public String bankEndpoint() {
        return "Bank access successful";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Admin access successful";
    }
}