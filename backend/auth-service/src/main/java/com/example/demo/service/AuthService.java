package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.demo.dto.DealerProfileResponse;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.ChangePasswordRequest;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserProfileResponse;
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
	
	public UserProfileResponse getProfile() {

	    Authentication authentication = SecurityContextHolder
	            .getContext()
	            .getAuthentication();

	    String email = authentication.getName();

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Common response for every user
	    if (user.getRole() != Role.DEALER) {

	        UserProfileResponse response = new UserProfileResponse();

	        response.setId(user.getId());
	        response.setFirstName(user.getFirstName());
	        response.setLastName(user.getLastName());
	        response.setEmail(user.getEmail());
	        response.setMobile(user.getMobile());
	        response.setCity(user.getCity());
	        response.setState(user.getState());
	        response.setRole(user.getRole().name());

	        return response;
	    }

	    // Dealer response
	    Dealer dealer = dealerRepository.findByUser(user)
	            .orElseThrow(() -> new RuntimeException("Dealer profile not found"));

	    DealerProfileResponse response = new DealerProfileResponse();

	    // User fields
	    response.setId(user.getId());
	    response.setFirstName(user.getFirstName());
	    response.setLastName(user.getLastName());
	    response.setEmail(user.getEmail());
	    response.setMobile(user.getMobile());
	    response.setCity(user.getCity());
	    response.setState(user.getState());
	    response.setRole(user.getRole().name());

	    // Dealer fields
	    response.setShowroomName(dealer.getShowroomName());
	    response.setGstNumber(dealer.getGstNumber());
	    response.setLicenseNumber(dealer.getLicenseNumber());
	    response.setAddress(dealer.getAddress());
	    response.setPinCode(dealer.getPinCode());
	    response.setContactPhone(dealer.getContactPhone());
	    response.setWorkingHours(dealer.getWorkingHours());
	    response.setRating(dealer.getRating());

	    return response;
	}
	
	@Transactional
	public UserProfileResponse updateProfile(UpdateProfileRequest request) {

	    Authentication authentication = SecurityContextHolder
	            .getContext()
	            .getAuthentication();

	    String email = authentication.getName();

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Update common user fields
	    user.setFirstName(request.getFirstName().trim());
	    user.setLastName(request.getLastName().trim());
	    user.setMobile(request.getMobile().trim());
	    user.setCity(request.getCity().trim());
	    user.setState(request.getState().trim());

	    userRepository.save(user);

	    // Update dealer details if user is a dealer
	    if (user.getRole() == Role.DEALER) {

	        Dealer dealer = dealerRepository.findByUser(user)
	                .orElseThrow(() -> new RuntimeException("Dealer profile not found"));

	        dealer.setShowroomName(request.getShowroomName() != null
	                ? request.getShowroomName().trim()
	                : null);

	        dealer.setAddress(request.getAddress() != null
	                ? request.getAddress().trim()
	                : null);

	        dealer.setPinCode(request.getPinCode() != null
	                ? request.getPinCode().trim()
	                : null);

	        dealer.setContactPhone(request.getContactPhone() != null
	                ? request.getContactPhone().trim()
	                : null);

	        dealer.setWorkingHours(request.getWorkingHours() != null
	                ? request.getWorkingHours().trim()
	                : null);

	        dealerRepository.save(dealer);
	    }

	    // Return updated profile
	    return getProfile();
	}
	
	@Transactional
	public String changePassword(ChangePasswordRequest request) {

	    Authentication authentication = SecurityContextHolder
	            .getContext()
	            .getAuthentication();

	    String email = authentication.getName();

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Verify current password
	    if (!passwordEncoder.matches(request.getCurrentPassword(),
	            user.getPasswordHash())) {

	        throw new IllegalArgumentException("Current password is incorrect");
	    }

	    // Verify new password confirmation
	    if (!request.getNewPassword()
	            .equals(request.getConfirmPassword())) {

	        throw new IllegalArgumentException("Passwords do not match");
	    }

	    // Prevent same password reuse
	    if (passwordEncoder.matches(request.getNewPassword(),
	            user.getPasswordHash())) {

	        throw new IllegalArgumentException(
	                "New password must be different from current password");
	    }

	    user.setPasswordHash(
	            passwordEncoder.encode(request.getNewPassword()));

	    userRepository.save(user);

	    return "Password changed successfully";
	}
}