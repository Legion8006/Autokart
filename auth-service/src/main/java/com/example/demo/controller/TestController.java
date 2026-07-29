package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/customer")
    public String customer() {
        return "Customer API";
    }

    @GetMapping("/dealer")
    public String dealer() {
        return "Dealer API";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin API";
    }

    @GetMapping("/protected")
    public String protectedApi() {
        return "Protected API";
    }
}