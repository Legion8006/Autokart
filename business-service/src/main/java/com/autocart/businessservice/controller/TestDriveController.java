package com.autocart.businessservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autocart.businessservice.entity.TestDriveBooking;
import com.autocart.businessservice.entity.CarVariant;
import com.autocart.businessservice.repository.TestDriveBookingRepository;
import com.autocart.businessservice.repository.CarVariantRepository;
import com.autocart.businessservice.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtService jwtService;

    public TestDriveController(
            TestDriveBookingRepository bookingRepository,
            CarVariantRepository variantRepository,
            JwtService jwtService) {
        this.bookingRepository = bookingRepository;
        this.variantRepository = variantRepository;
        this.jwtService = jwtService;
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
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {
        TestDriveBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + id));

        String token = extractToken(request);
        if (token != null && jwtService.isTokenValid(token)) {
            String role = jwtService.extractRole(token);
            if (role != null && !role.toUpperCase().contains("ADMIN")) {
                String reqDealerIdStr = body.get("dealerId");
                if (reqDealerIdStr != null) {
                    Long reqDealerId = Long.parseLong(reqDealerIdStr);
                    if (!booking.getDealerId().equals(reqDealerId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("error", "Unauthorized: Dealer cannot modify test drive for another showroom."));
                    }
                }
            }
        }

        booking.setStatus(body.getOrDefault("status", "CONFIRMED").toUpperCase());
        if (body.containsKey("cancellationReason")) {
            booking.setCancellationReason(body.get("cancellationReason"));
        }
        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
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
