package com.autocart.businessservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autocart.businessservice.entity.TestDriveBooking;
import java.util.List;

public interface TestDriveBookingRepository extends JpaRepository<TestDriveBooking, Long> {
    List<TestDriveBooking> findByUserId(Long userId);
    List<TestDriveBooking> findByDealerId(Long dealerId);
}
