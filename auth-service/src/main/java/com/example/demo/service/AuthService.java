package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Dealer;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import com.example.demo.entity.DealerStatus;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.repositories.DealerRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtService;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final CustomUserDetailsService userDetailsService;
	private final JwtService jwtService;
	private final DealerRepository dealerRepository;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
			JwtService jwtService, DealerRepository dealerRepository) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;

		// This was missing
		this.dealerRepository = dealerRepository;
	}
	
	public UserResponse getCurrentUser(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return new UserResponse(
	            user.getId(),
	            user.getFirstName(),
	            user.getLastName(),
	            user.getEmail(),
	            user.getRole()
	    );
	}

	// REGISTER
	@Transactional
	public String register(RegisterRequest request) {

		// 1. Prevent public ADMIN registration
		if (request.getRole() == Role.ADMIN) {
			throw new IllegalArgumentException("Admin registration is not allowed");
		}

		// 2. Normalize email before duplicate check
		String email = request.getEmail().trim().toLowerCase();

		// 3. Check duplicate email
		if (userRepository.existsByEmail(email)) {
			throw new EmailAlreadyExistsException("Email already registered");
		}

		// 4. Validate dealer-specific information
		if (request.getRole() == Role.DEALER) {

			if (request.getShowroomName() == null || request.getShowroomName().isBlank()) {

				throw new IllegalArgumentException("Showroom name is required for dealer");
			}

			if (request.getGstNumber() == null || request.getGstNumber().isBlank()) {

				throw new IllegalArgumentException("GST number is required for dealer");
			}

			if (request.getLicenseNumber() == null || request.getLicenseNumber().isBlank()) {

				throw new IllegalArgumentException("License number is required for dealer");
			}

			// Normalize dealer values before checking duplicates
			String gstNumber = request.getGstNumber().trim().toUpperCase();

			if (gstNumber.length() != 15) {
				throw new IllegalArgumentException("GST number must be exactly 15 characters");
			}

			String licenseNumber = request.getLicenseNumber().trim().toUpperCase();

			if (licenseNumber.length() < 9 || licenseNumber.length() > 11) {

				throw new IllegalArgumentException("License number must be between 9 and 11 characters");
			}

			if (dealerRepository.existsByLicenseNumber(licenseNumber)) {

				throw new IllegalArgumentException("License number already registered");
			}

			if (dealerRepository.existsByGstNumber(gstNumber)) {
				throw new IllegalArgumentException("GST number already registered");
			}
		}

		// 5. Create common user
		User user = new User();

		user.setFirstName(request.getFirstName().trim());

		user.setLastName(request.getLastName().trim());

		user.setEmail(email);

		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

		user.setMobile(request.getMobile().trim());

		user.setCity(request.getCity().trim());

		user.setState(request.getState().trim());

		user.setRole(request.getRole());

		user.setStatus(UserStatus.ACTIVE);

		User savedUser = userRepository.save(user);

		// 6. Create dealer profile only for DEALER
		if (request.getRole() == Role.DEALER) {

			Dealer dealer = new Dealer();

			dealer.setUser(savedUser);

			dealer.setShowroomName(request.getShowroomName().trim());

			dealer.setGstNumber(request.getGstNumber().trim().toUpperCase());

			dealer.setLicenseNumber(request.getLicenseNumber().trim().toUpperCase());

			dealer.setAddress(request.getAddress());

			dealer.setCity(request.getCity());

			dealer.setState(request.getState());

			dealer.setPinCode(request.getPinCode());

			dealer.setContactPhone(request.getContactPhone());

			dealer.setWorkingHours(request.getWorkingHours());

			dealer.setRating(0.0f);

			dealer.setStatus(DealerStatus.PENDING);

			dealerRepository.save(dealer);
		}

		return "Registration successful";
	}

	// LOGIN
	public AuthResponse login(LoginRequest request) {

		String email = request.getEmail().trim().toLowerCase();

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
		} catch (Exception e) {
			e.printStackTrace(); // VERY IMPORTANT
			throw e;
		}

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		String token = jwtService.generateToken(user);

		return new AuthResponse(token);
	}
}