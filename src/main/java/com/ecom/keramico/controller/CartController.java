package com.ecom.keramico.controller;

import com.ecom.keramico.model.Cart;
import com.ecom.keramico.model.User;
import com.ecom.keramico.service.CartService;
import com.ecom.keramico.service.UserDetailsServiceImpl;
import com.ecom.keramico.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    private final CartService cartService;
    private final UserService userService; // Предполагается, что у вас есть сервис для работы с пользователями

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        User user = userService.getCurrentUser(); // Метод для получения текущего пользователя
        Cart cart = cartService.getCartByUser(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam Long productId,
                                          @RequestParam(defaultValue = "1") int quantity) {
        User user = userService.getCurrentUser();
        Cart cart = cartService.addProductToCart(user, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@RequestParam Long productId) {
        User user = userService.getCurrentUser();
        Cart cart = cartService.removeProductFromCart(user, productId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/update")
    public ResponseEntity<Cart> updateQuantity(@RequestParam Long productId,
                                               @RequestParam int quantity) {
        User user = userService.getCurrentUser();
        Cart cart = cartService.updateProductQuantity(user, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/clear")
    public ResponseEntity<Cart> clearCart() {
        User user = userService.getCurrentUser();
        Cart cart = cartService.clearCart(user);
        return ResponseEntity.ok(cart);
    }
}
