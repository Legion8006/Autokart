package com.autocart.businessservice.repository;

import com.autocart.businessservice.entity.CarModel;
import com.autocart.businessservice.entity.CarVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarVariantRepository extends JpaRepository<CarVariant, String> {

    List<CarVariant> findByModel(CarModel model);

    Page<CarVariant> findAll(Pageable pageable);

}