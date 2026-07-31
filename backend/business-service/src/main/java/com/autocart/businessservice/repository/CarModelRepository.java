package com.autocart.businessservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autocart.businessservice.entity.Brand;
import com.autocart.businessservice.entity.CarModel;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, String> {

    List<CarModel> findByBrand(Brand brand);

}