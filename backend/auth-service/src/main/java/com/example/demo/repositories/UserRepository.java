package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
