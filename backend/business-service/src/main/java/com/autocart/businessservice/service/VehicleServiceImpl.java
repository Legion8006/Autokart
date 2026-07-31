package com.autocart.businessservice.service;

import com.autocart.businessservice.dto.VehicleCardResponse;
import com.autocart.businessservice.dto.VehiclePageResponse;
import com.autocart.businessservice.entity.CarVariant;
import com.autocart.businessservice.entity.ImageType;
import com.autocart.businessservice.entity.VariantImage;
import com.autocart.businessservice.repository.CarVariantRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

	@Autowired
	private CarVariantRepository carVariantRepository;

	@Override
	@Transactional(readOnly = true)
	public VehiclePageResponse getAllVehicles(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		Page<CarVariant> variantPage = carVariantRepository.findAll(pageable);

		List<VehicleCardResponse> vehicleCards = new ArrayList<>();

		for (CarVariant variant : variantPage.getContent()) {

			VehicleCardResponse response = new VehicleCardResponse();

			response.setVariantId(variant.getId());

			response.setBrand(variant.getModel().getBrand().getName());

			response.setModel(variant.getModel().getName());

			response.setVariant(variant.getVariantName());

			response.setBasePrice(variant.getBasePrice());
			
			response.setFuelType(variant.getFuelType());

			response.setTransmission(variant.getTransmission());

			String thumbnail = null;

			if (variant.getImages() != null) {

				for (VariantImage image : variant.getImages()) {

					if (image.getImageType() == ImageType.EXTERIOR) {
						thumbnail = image.getImageUrl();
						break;
					}
				}
			}

			response.setThumbnail(thumbnail);

			vehicleCards.add(response);
		}

		VehiclePageResponse pageResponse = new VehiclePageResponse();

		pageResponse.setContent(vehicleCards);
		pageResponse.setPage(variantPage.getNumber());
		pageResponse.setSize(variantPage.getSize());
		pageResponse.setTotalElements(variantPage.getTotalElements());
		pageResponse.setTotalPages(variantPage.getTotalPages());

		return pageResponse;
	}
}