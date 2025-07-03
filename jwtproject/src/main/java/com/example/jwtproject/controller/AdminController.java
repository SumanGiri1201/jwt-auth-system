package com.example.jwtproject.controller;

import com.example.jwtproject.dto.RoleChangeRequest;
import com.example.jwtproject.model.User;
import com.example.jwtproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepo;


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    @PostMapping("/change-role")
    public String changeUserRole(@RequestBody RoleChangeRequest request) {
        Optional<User> optionalUser = userRepo.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return "User not found";
        }

        User user = optionalUser.get();
        user.setRoles(Set.of("ROLE_" + request.getRole().toUpperCase()));
        userRepo.save(user);

        return "Role updated to " + request.getRole().toUpperCase();
    }
}
