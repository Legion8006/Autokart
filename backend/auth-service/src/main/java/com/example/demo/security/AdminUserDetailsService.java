package com.example.demo.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.repositories.AdminRepository;

//@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Admin admin = adminRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Admin not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password(admin.getPasswordHash())
                .roles(admin.getRole().name())
                .build();
    }
}