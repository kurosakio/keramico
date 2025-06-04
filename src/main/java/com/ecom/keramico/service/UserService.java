package com.ecom.keramico.service;

import com.ecom.keramico.model.User;
import com.ecom.keramico.model.UserRole;
import com.ecom.keramico.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Метод для получения текущего аутентифицированного пользователя
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    // Метод для регистрации нового пользователя (можно перенести из AuthController)
    public User registerUser(User regUser) {
        if (userRepository.existsByUsername(regUser.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder()
                .username(regUser.getUsername())
                .hashed_password(passwordEncoder.encode(regUser.getPassword()))
                .role(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    // Метод для обновления профиля пользователя
    public User updateUserProfile(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (updatedUser.getFullname() != null) {
            existingUser.setFullname(updatedUser.getFullname());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setHashed_password(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    // Метод для поиска пользователя по имени
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Метод для получения пользователя по ID
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}
