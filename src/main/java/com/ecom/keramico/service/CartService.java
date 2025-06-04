package com.ecom.keramico.service;

import com.ecom.keramico.model.Cart;
import com.ecom.keramico.model.CartItem;
import com.ecom.keramico.model.Product;
import com.ecom.keramico.model.User;
import com.ecom.keramico.repository.CartItemRepository;
import com.ecom.keramico.repository.CartRepository;
import com.ecom.keramico.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public Cart addProductToCart(User user, Long productId, int quantity) {
        Cart cart = getCartByUser(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Проверяем, есть ли уже такой товар в корзине
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Увеличиваем количество существующего товара
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.calculateTotalPrice();
            cartItemRepository.save(item);
        } else {
            // Добавляем новый товар в корзину
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            newItem.calculateTotalPrice();
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        cart.calculateTotalPrice();
        return cartRepository.save(cart);
    }

    public Cart removeProductFromCart(User user, Long productId) {
        Cart cart = getCartByUser(user);
        Optional<CartItem> itemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (itemToRemove.isPresent()) {
            cart.removeItem(itemToRemove.get());
            cartItemRepository.delete(itemToRemove.get());
            cartRepository.save(cart);
        }

        return cart;
    }

    public Cart updateProductQuantity(User user, Long productId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeProductFromCart(user, productId);
        }

        Cart cart = getCartByUser(user);
        Optional<CartItem> itemToUpdate = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (itemToUpdate.isPresent()) {
            CartItem item = itemToUpdate.get();
            item.setQuantity(newQuantity);
            item.calculateTotalPrice();
            cartItemRepository.save(item);
            cart.calculateTotalPrice();
            cartRepository.save(cart);
        }

        return cart;
    }

    public Cart clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getCartItems().forEach(item -> cartItemRepository.delete(item));
        cart.clear();
        return cartRepository.save(cart);
    }


}
