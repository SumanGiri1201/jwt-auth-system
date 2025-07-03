package com.example.jwtproject;

import com.example.jwtproject.model.User;
import com.example.jwtproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class JwtprojectApplication {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(JwtprojectApplication.class, args);
	}

	@PostConstruct
	public void createAdminUser() {
		String adminEmail = "admin@example.com";
		String adminPassword = "admin123";

		if (userRepo.findByEmail(adminEmail).isEmpty()) {
			User admin = new User();
			admin.setEmail(adminEmail);
			admin.setPassword(passwordEncoder.encode(adminPassword));
			admin.setRoles(Set.of("ROLE_ADMIN"));
			userRepo.save(admin);

			System.out.println("Default admin created:");
			System.out.println("Email: " + adminEmail);
			System.out.println("Password: " + adminPassword);
		} else {
			System.out.println("Admin already exists: " + adminEmail);
		}
	}
}
