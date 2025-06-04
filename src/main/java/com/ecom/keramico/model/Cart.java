package com.ecom.keramico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private int totalPrice = 0;

    public void addItem(CartItem item) {
        cartItems.add(item);
        item.setCart(this);
        totalPrice += item.getTotalPrice();
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
        item.setCart(null);
        totalPrice -= item.getTotalPrice();
    }

    public void clear() {
        for (CartItem item : cartItems) {
            item.setCart(null);
        }
        cartItems.clear();
        totalPrice = 0;
    }

    public int calculateTotalPrice() {
        this.totalPrice = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
        return this.totalPrice;
    }
}
