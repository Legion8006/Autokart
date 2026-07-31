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

		if (!adminRepository.existsByEmail(adminEmail)) {

			Admin admin = new Admin();

			admin.setFirstName("System");
			admin.setLastName("Administrator");

			admin.setEmail(adminEmail);

			admin.setPasswordHash(passwordEncoder.encode("Admin@123"));

			admin.setRole(AdminRole.SUPER_ADMIN);

			adminRepository.save(admin);

		} else {

			System.out.println("Default Admin already exists.");
		}
	}
}
