package com.ecom.keramico.controller;

import com.ecom.keramico.model.User;
import com.ecom.keramico.model.UserRole;
import com.ecom.keramico.repository.UserRepository;
import com.ecom.keramico.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User regUser) {
        try {
            User user = userService.registerUser(regUser);
            return ResponseEntity.ok("User successfully registered");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
