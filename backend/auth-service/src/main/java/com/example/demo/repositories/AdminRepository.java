package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByEmail(String email);
    
    boolean existsByEmail(String email);
}