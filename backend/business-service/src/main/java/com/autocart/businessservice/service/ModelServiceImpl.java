package com.autocart.businessservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autocart.businessservice.dto.ModelResponse;
import com.autocart.businessservice.entity.Brand;
import com.autocart.businessservice.entity.CarModel;
import com.autocart.businessservice.exception.ResourceNotFoundException;
import com.autocart.businessservice.repository.BrandRepository;
import com.autocart.businessservice.repository.CarModelRepository;
import com.autocart.businessservice.service.ModelService;

@Service
public class ModelServiceImpl implements ModelService {

    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;

    public ModelServiceImpl(BrandRepository brandRepository,
                            CarModelRepository carModelRepository) {
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
    }

    @Override
    public List<ModelResponse> getModelsByBrand(String brandId) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Brand not found"));

        List<CarModel> models = carModelRepository.findByBrand(brand);

        return models.stream()
                .map(this::mapToModelResponse)
                .toList();
    }

    private ModelResponse mapToModelResponse(CarModel model) {

        return new ModelResponse(
                model.getId(),
                model.getName(),
                model.getBodyType(),
                model.getLaunchYear()
        );
    }
}