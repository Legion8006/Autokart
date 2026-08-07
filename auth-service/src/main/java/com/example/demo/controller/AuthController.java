package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/me")
	public ResponseEntity<UserProfileResponse> getCurrentUser() {

		return ResponseEntity.ok(authService.getProfile());
	}

	@PutMapping("/me")
	public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {

		return ResponseEntity.ok(authService.updateProfile(request));
	}

	@PutMapping("/change-password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {

		return ResponseEntity.ok(authService.changePassword(request));
	}

	// REGISTER
	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {

		authService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
	}

	// LOGIN
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

		AuthResponse response = authService.login(request);

		ResponseCookie cookie = ResponseCookie.from("jwt", response.getToken()).httpOnly(true).secure(false) // true in
																												// production
																												// (HTTPS)
				.path("/").sameSite("Lax").maxAge(60 * 60 * 24).build();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
	}

	// LOGOUT
	@PostMapping("/logout")
	public ResponseEntity<String> logout() {

		ResponseCookie cookie = ResponseCookie.from("jwt", "").httpOnly(true).secure(false) // true in production
				.path("/").sameSite("Lax").maxAge(0).build();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Logged out successfully");
	}
}