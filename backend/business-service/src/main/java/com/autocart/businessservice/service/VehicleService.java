package com.autocart.businessservice.service;

import com.autocart.businessservice.dto.VehiclePageResponse;

public interface VehicleService {

    VehiclePageResponse getAllVehicles(int page, int size);

}