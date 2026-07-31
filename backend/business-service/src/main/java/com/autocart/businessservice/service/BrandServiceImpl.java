package com.autocart.businessservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autocart.businessservice.dto.BrandResponse;
import com.autocart.businessservice.entity.Brand;
import com.autocart.businessservice.repository.BrandRepository;
import com.autocart.businessservice.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

	private final BrandRepository brandRepository;

	public BrandServiceImpl(BrandRepository brandRepository) {
		this.brandRepository = brandRepository;
	}

	@Override
	public List<BrandResponse> getAllBrands() {

		List<Brand> brands = brandRepository.findAll();

		return brands.stream().map(this::mapToBrandResponse).toList();
	}

	private BrandResponse mapToBrandResponse(Brand brand) {

		return new BrandResponse(brand.getId(), brand.getName(), brand.getLogoUrl(), brand.getOriginCountry());
	}
}