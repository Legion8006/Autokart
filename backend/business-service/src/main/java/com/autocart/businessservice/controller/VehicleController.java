package com.autocart.businessservice.controller;

import com.autocart.businessservice.dto.VehiclePageResponse;
import com.autocart.businessservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<VehiclePageResponse> getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        VehiclePageResponse response = vehicleService.getAllVehicles(page, size);

        return ResponseEntity.ok(response);
    }
}