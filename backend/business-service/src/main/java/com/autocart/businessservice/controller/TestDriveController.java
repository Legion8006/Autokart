package com.autocart.businessservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autocart.businessservice.entity.TestDriveBooking;
import com.autocart.businessservice.entity.CarVariant;
import com.autocart.businessservice.repository.TestDriveBookingRepository;
import com.autocart.businessservice.repository.CarVariantRepository;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test-drives")
public class TestDriveController {

    private final TestDriveBookingRepository bookingRepository;
    private final CarVariantRepository variantRepository;

    public TestDriveController(TestDriveBookingRepository bookingRepository, CarVariantRepository variantRepository) {
        this.bookingRepository = bookingRepository;
        this.variantRepository = variantRepository;
    }

    @PostMapping
    public ResponseEntity<TestDriveBooking> bookTestDrive(@RequestBody TestDriveRequest request) {
        CarVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found: " + request.getVariantId()));

        TestDriveBooking booking = new TestDriveBooking();
        booking.setUserId(request.getUserId());
        booking.setDealerId(request.getDealerId());
        booking.setVariant(variant);
        booking.setPreferredDate(request.getPreferredDate());
        booking.setPreferredTime(request.getPreferredTime());
        booking.setStatus("PENDING");

        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TestDriveBooking>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingRepository.findByUserId(userId));
    }

    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<List<TestDriveBooking>> getDealerBookings(@PathVariable Long dealerId) {
        return ResponseEntity.ok(bookingRepository.findByDealerId(dealerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TestDriveBooking> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        TestDriveBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + id));

        booking.setStatus(body.getOrDefault("status", "CONFIRMED").toUpperCase());
        if (body.containsKey("cancellationReason")) {
            booking.setCancellationReason(body.get("cancellationReason"));
        }
        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    @Getter
    @Setter
    public static class TestDriveRequest {
        private Long userId;
        private Long dealerId;
        private Long variantId;
        private LocalDate preferredDate;
        private String preferredTime;
    }
}
