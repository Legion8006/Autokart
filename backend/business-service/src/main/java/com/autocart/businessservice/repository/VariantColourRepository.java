package com.autocart.businessservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autocart.businessservice.entity.VariantColour;
import java.util.List;

public interface VariantColourRepository extends JpaRepository<VariantColour, Long> {
    List<VariantColour> findByVariantId(Long variantId);
}
