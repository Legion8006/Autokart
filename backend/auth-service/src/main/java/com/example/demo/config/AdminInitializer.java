package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Admin;
import com.example.demo.entity.AdminRole;
import com.example.demo.repositories.AdminRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {

		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {
		String adminEmail = "admin@autocart.com";

		Admin admin = adminRepository.findByEmail(adminEmail).orElseGet(() -> {
			Admin newAdmin = new Admin();
			newAdmin.setEmail(adminEmail);
			return newAdmin;
		});

		admin.setFirstName("System");
		admin.setLastName("Administrator");
		admin.setPasswordHash(passwordEncoder.encode("admin123"));
		admin.setRole(AdminRole.SUPER_ADMIN);

		adminRepository.save(admin);
		System.out.println("Default Admin initialized successfully with email: admin@autocart.com");
	}
}
