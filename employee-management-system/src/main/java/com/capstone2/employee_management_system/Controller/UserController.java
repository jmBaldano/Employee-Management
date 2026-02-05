package com.capstone2.employee_management_system.Controller;

import com.capstone2.employee_management_system.Model.AdminModel;
import com.capstone2.employee_management_system.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    //register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AdminModel user) {
        try {
            log.info("Register attempt for username={}", user.getUsername());
            userService.register(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException ex) {
            log.warn("Register failed: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // optional GET handler to avoid 403s on accidental GETs to /auth/register
    @GetMapping("/register")
    public ResponseEntity<String> registerGet() {
        return ResponseEntity.ok("Register endpoint (POST) expects JSON payload");
    }

    // login endpoint

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminModel user, HttpServletRequest request) {
        try {
            // userService authenticare
            Authentication auth = userService.login(user.getUsername(), user.getPassword());

            // store authentication in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(auth);

            //get the  current session
            request.getSession(true);

            return ResponseEntity.ok("Login successful!");
        }
        catch (Exception ex) {
            return ResponseEntity.status(401).body("Invalid username or password!");
        }
    }

}
