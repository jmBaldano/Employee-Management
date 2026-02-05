package com.capstone2.employee_management_system.Service;

import com.capstone2.employee_management_system.Model.AdminModel;
import com.capstone2.employee_management_system.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service layer for User authentication and registration
 * - Contains business logic
 * - Handles password encoding and authentication
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void register(AdminModel user) {

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new RuntimeException("Username and password are required");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Authentication login(String username, String password) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // will throw AuthenticationException if invalid
        return authenticationManager.authenticate(token);
    }
    // checks if the username is in database
    public AdminModel findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
