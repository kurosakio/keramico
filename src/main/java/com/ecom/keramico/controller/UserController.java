package com.ecom.keramico.controller;

import com.ecom.keramico.model.User;
import com.ecom.keramico.repository.UserRepository;
import com.ecom.keramico.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> showUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping
    public ResponseEntity<User> updateProfile(@RequestBody User updatedUser) {
        User currentUser = userService.getCurrentUser();
        User updated = userService.updateUserProfile(currentUser.getId(), updatedUser);
        return ResponseEntity.ok(updated);
    }
}
