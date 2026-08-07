package com.autocart.businessservice.service;

import java.math.BigDecimal;

import com.autocart.businessservice.dto.VehiclePageResponse;
import com.autocart.businessservice.entity.FuelType;

public interface VehicleService {

    VehiclePageResponse getAllVehicles(int page, int size, String brand, FuelType fuelType, BigDecimal minPrice, BigDecimal maxPrice);

}