package com.autocart.businessservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autocart.businessservice.dto.ModelResponse;
import com.autocart.businessservice.service.ModelService;

@RestController
@RequestMapping("/api/brands")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping("/{brandId}/models")
    public List<ModelResponse> getModelsByBrand(
            @PathVariable String brandId) {

        return modelService.getModelsByBrand(brandId);
    }
}