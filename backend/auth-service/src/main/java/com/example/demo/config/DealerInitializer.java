package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.User;
import com.example.demo.entity.Dealer;
import com.example.demo.entity.DealerBrand;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserStatus;
import com.example.demo.entity.DealerStatus;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.DealerRepository;
import com.example.demo.repositories.DealerBrandRepository;

@Component
public class DealerInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DealerRepository dealerRepository;
    private final DealerBrandRepository dealerBrandRepository;
    private final PasswordEncoder passwordEncoder;

    public DealerInitializer(
            UserRepository userRepository,
            DealerRepository dealerRepository,
            DealerBrandRepository dealerBrandRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dealerRepository = dealerRepository;
        this.dealerBrandRepository = dealerBrandRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String dealerEmail = "dealer@bubhandari.com";

        // Create or update approved test dealer
        User user = userRepository.findByEmail(dealerEmail).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(dealerEmail);
            return newUser;
        });

        user.setFirstName("BU Bhandari");
        user.setLastName("Motors");
        user.setPasswordHash(passwordEncoder.encode("dealer123"));
        user.setMobile("9822012345");
        user.setCity("Pune");
        user.setState("Maharashtra");
        user.setRole(Role.DEALER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        Dealer dealer = dealerRepository.findByUser(savedUser).orElseGet(() -> {
            Dealer newDealer = new Dealer();
            newDealer.setUser(savedUser);
            return newDealer;
        });

        dealer.setShowroomName("B U Bhandari Maruti Suzuki");
        dealer.setGstNumber("27AAACB1234F1Z5");
        dealer.setLicenseNumber("LIC-MB-99881");
        dealer.setAddress("123 Wakdewadi, Pune-Mumbai Highway");
        dealer.setCity("Pune");
        dealer.setState("Maharashtra");
        dealer.setPinCode("411005");
        dealer.setContactPhone("020-25531234");
        dealer.setWorkingHours("09:00 AM - 08:00 PM");
        dealer.setRating(4.8f);
        dealer.setStatus(DealerStatus.APPROVED);

        Dealer savedDealer = dealerRepository.save(dealer);

        if (dealerBrandRepository.findByDealer(savedDealer).isEmpty()) {
            DealerBrand db = new DealerBrand();
            db.setDealer(savedDealer);
            db.setBrandId(1L); // Maruti Suzuki
            db.setStatus(DealerStatus.APPROVED);
            dealerBrandRepository.save(db);
        }
    }
}
