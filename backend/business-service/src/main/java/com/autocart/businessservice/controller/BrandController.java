package com.autocart.businessservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autocart.businessservice.dto.BrandResponse;
import com.autocart.businessservice.service.BrandService;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

	private final BrandService brandService;

	public BrandController(BrandService brandService) {
		this.brandService = brandService;
	}

	@GetMapping
	public List<BrandResponse> getAllBrands() {
		return brandService.getAllBrands();
	}
}