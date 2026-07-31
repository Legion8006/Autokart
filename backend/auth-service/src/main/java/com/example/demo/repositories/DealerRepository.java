package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Dealer;
import com.example.demo.entity.User;

public interface DealerRepository
        extends JpaRepository<Dealer, String> {

    boolean existsByGstNumber(String gstNumber);

    boolean existsByLicenseNumber(String licenseNumber);

    Optional<Dealer> findByUser(User userId);
}