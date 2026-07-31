package com.autocart.businessservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.autocart.businessservice.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
	Optional<Brand> findById(String id);
}
