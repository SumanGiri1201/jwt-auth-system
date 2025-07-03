package com.example.jwtproject.controller;

import com.example.jwtproject.dto.LoginRequest;
import com.example.jwtproject.dto.SignupRequest;
import com.example.jwtproject.model.User;
import com.example.jwtproject.repository.UserRepository;
import com.example.jwtproject.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        Optional<User> existing = userRepo.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            return "User already exists";
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of("ROLE_USER")); // default role

        userRepo.save(user);
        return "Signup successful";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            return "Invalid credentials";
        }


        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().toArray(new String[0]))
                .build();

        return jwtService.generateToken(userDetails);
    }
}
