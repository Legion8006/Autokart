package com.autocart.businessservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autocart.businessservice.entity.DealerInventory;
import com.autocart.businessservice.entity.DealerOffer;
import com.autocart.businessservice.entity.CarVariant;
import com.autocart.businessservice.repository.DealerInventoryRepository;
import com.autocart.businessservice.repository.DealerOfferRepository;
import com.autocart.businessservice.repository.CarVariantRepository;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DealerInventoryController {

    private final DealerInventoryRepository inventoryRepository;
    private final DealerOfferRepository offerRepository;
    private final CarVariantRepository variantRepository;

    public DealerInventoryController(
            DealerInventoryRepository inventoryRepository,
            DealerOfferRepository offerRepository,
            CarVariantRepository variantRepository) {
        this.inventoryRepository = inventoryRepository;
        this.offerRepository = offerRepository;
        this.variantRepository = variantRepository;
    }

    @PostMapping("/dealer/inventory")
    public ResponseEntity<DealerInventory> createInventory(@RequestBody InventoryRequest request) {
        CarVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found: " + request.getVariantId()));

        DealerInventory inventory = new DealerInventory();
        inventory.setDealerId(request.getDealerId());
        inventory.setVariant(variant);
        inventory.setListedPrice(request.getListedPrice());
        inventory.setDiscountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO);
        inventory.setDiscountType(request.getDiscountType() != null ? request.getDiscountType() : "FLAT");
        inventory.setUnitsAvailable(request.getUnitsAvailable() != null ? request.getUnitsAvailable() : 1);
        inventory.setDeliveryDays(request.getDeliveryDays() != null ? request.getDeliveryDays() : 7);
        inventory.setApprovalStatus("PENDING");
        inventory.setListingStatus("ACTIVE");

        DealerInventory saved = inventoryRepository.save(inventory);

        if (request.getOffers() != null) {
            for (OfferRequest oReq : request.getOffers()) {
                DealerOffer offer = new DealerOffer();
                offer.setInventory(saved);
                offer.setOfferType(oReq.getOfferType());
                offer.setOfferDescription(oReq.getOfferDescription());
                offerRepository.save(offer);
            }
        }

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/dealer/inventory")
    public ResponseEntity<List<DealerInventory>> getDealerInventory(@RequestParam Long dealerId) {
        return ResponseEntity.ok(inventoryRepository.findByDealerId(dealerId));
    }

    @GetMapping("/vehicles/variant/{variantId}/dealers")
    public ResponseEntity<List<DealerInventory>> getApprovedDealersForVariant(@PathVariable Long variantId) {
        List<DealerInventory> list = inventoryRepository.findByVariantIdAndApprovalStatusAndListingStatus(
                variantId, "APPROVED", "ACTIVE");
        return ResponseEntity.ok(list);
    }

    @GetMapping("/admin/inventory/pending")
    public ResponseEntity<List<DealerInventory>> getPendingInventoryListings() {
        return ResponseEntity.ok(inventoryRepository.findByApprovalStatus("PENDING"));
    }

    @PutMapping("/admin/inventory/{inventoryId}/status")
    public ResponseEntity<DealerInventory> updateInventoryStatus(
            @PathVariable Long inventoryId,
            @RequestBody Map<String, String> body) {
        DealerInventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory listing not found: " + inventoryId));

        String status = body.getOrDefault("status", "APPROVED");
        String rejectionReason = body.get("rejectionReason");

        inventory.setApprovalStatus(status.toUpperCase());
        if (rejectionReason != null) {
            inventory.setRejectionReason(rejectionReason);
        }
        return ResponseEntity.ok(inventoryRepository.save(inventory));
    }

    @Getter
    @Setter
    public static class InventoryRequest {
        private Long dealerId;
        private Long variantId;
        private BigDecimal listedPrice;
        private BigDecimal discountAmount;
        private String discountType;
        private Integer unitsAvailable;
        private Integer deliveryDays;
        private List<OfferRequest> offers;
    }

    @Getter
    @Setter
    public static class OfferRequest {
        private String offerType;
        private String offerDescription;
    }
}
