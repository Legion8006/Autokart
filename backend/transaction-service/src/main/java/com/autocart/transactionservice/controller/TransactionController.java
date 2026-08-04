package com.autocart.transactionservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autocart.transactionservice.entity.BankPartner;
import com.autocart.transactionservice.entity.EmiProduct;
import com.autocart.transactionservice.entity.CardOffer;
import com.autocart.transactionservice.entity.PurchaseEnquiry;
import com.autocart.transactionservice.repository.BankPartnerRepository;
import com.autocart.transactionservice.repository.EmiProductRepository;
import com.autocart.transactionservice.repository.CardOfferRepository;
import com.autocart.transactionservice.repository.PurchaseEnquiryRepository;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final BankPartnerRepository bankPartnerRepository;
    private final EmiProductRepository emiProductRepository;
    private final CardOfferRepository cardOfferRepository;
    private final PurchaseEnquiryRepository purchaseEnquiryRepository;

    public TransactionController(
            BankPartnerRepository bankPartnerRepository,
            EmiProductRepository emiProductRepository,
            CardOfferRepository cardOfferRepository,
            PurchaseEnquiryRepository purchaseEnquiryRepository) {
        this.bankPartnerRepository = bankPartnerRepository;
        this.emiProductRepository = emiProductRepository;
        this.cardOfferRepository = cardOfferRepository;
        this.purchaseEnquiryRepository = purchaseEnquiryRepository;
    }

    @GetMapping("/banks")
    public ResponseEntity<List<BankPartner>> getAllBanks() {
        return ResponseEntity.ok(bankPartnerRepository.findAll());
    }

    @GetMapping("/emi-products")
    public ResponseEntity<List<EmiProduct>> getEmiProducts() {
        return ResponseEntity.ok(emiProductRepository.findAll());
    }

    @GetMapping("/card-offers")
    public ResponseEntity<List<CardOffer>> getCardOffers() {
        return ResponseEntity.ok(cardOfferRepository.findAll());
    }

    @PostMapping("/enquiry")
    public ResponseEntity<PurchaseEnquiry> createEnquiry(@RequestBody EnquiryRequest request) {
        PurchaseEnquiry enquiry = new PurchaseEnquiry();
        enquiry.setUserId(request.getUserId());
        enquiry.setDealerId(request.getDealerId());
        enquiry.setInventoryId(request.getInventoryId());
        enquiry.setSelectedColour(request.getSelectedColour());
        enquiry.setFinancePreference(request.getFinancePreference() != null ? request.getFinancePreference() : "CASH");
        enquiry.setEmiProductId(request.getEmiProductId());
        enquiry.setCardOfferId(request.getCardOfferId());
        enquiry.setCardOfferApplied(request.getCardOfferApplied());
        enquiry.setFinalPriceShown(request.getFinalPriceShown());

        return ResponseEntity.ok(purchaseEnquiryRepository.save(enquiry));
    }

    @GetMapping("/enquiries/user/{userId}")
    public ResponseEntity<List<PurchaseEnquiry>> getUserEnquiries(@PathVariable Long userId) {
        return ResponseEntity.ok(purchaseEnquiryRepository.findByUserId(userId));
    }

    @GetMapping("/enquiries/dealer/{dealerId}")
    public ResponseEntity<List<PurchaseEnquiry>> getDealerEnquiries(@PathVariable Long dealerId) {
        return ResponseEntity.ok(purchaseEnquiryRepository.findByDealerId(dealerId));
    }

    @PutMapping("/enquiries/{id}/status")
    public ResponseEntity<PurchaseEnquiry> updateEnquiryStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        PurchaseEnquiry enquiry = purchaseEnquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enquiry not found: " + id));

        String status = body.getOrDefault("status", "PROCESSED").toUpperCase();
        enquiry.setStatus(status);
        return ResponseEntity.ok(purchaseEnquiryRepository.save(enquiry));
    }

    @Getter
    @Setter
    public static class EnquiryRequest {
        private Long userId;
        private Long dealerId;
        private Long inventoryId;
        private String selectedColour;
        private String financePreference;
        private Long emiProductId;
        private Long cardOfferId;
        private String cardOfferApplied;
        private BigDecimal finalPriceShown;
    }
}
