package com.school.ecommerce.controller;

import com.school.ecommerce.dto.CartItemDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController {

    @SuppressWarnings("unchecked")
    private List<CartItemDto> getCart(HttpSession session) {
        List<CartItemDto> cart = (List<CartItemDto>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @SuppressWarnings("unchecked")
    private List<Long> getFavorites(HttpSession session) {
        List<Long> favs = (List<Long>) session.getAttribute("favorites");
        if (favs == null) {
            favs = new ArrayList<>();
            session.setAttribute("favorites", favs);
        }
        return favs;
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDto>> getCartItems(HttpSession session) {
        return ResponseEntity.ok(getCart(session));
    }

    @PostMapping("/cart/add")
    public ResponseEntity<List<CartItemDto>> addToCart(@RequestBody CartItemDto item, HttpSession session) {
        List<CartItemDto> cart = getCart(session);
        boolean found = false;
        for (CartItemDto cartItem : cart) {
            if (cartItem.getId().equals(item.getId()) && Objects.equals(cartItem.getSelectedSize(), item.getSelectedSize())) {
                cartItem.setQty(cartItem.getQty() + 1);
                found = true;
                break;
            }
        }
        if (!found) {
            if (item.getQty() == null) item.setQty(1);
            cart.add(item);
        }
        session.setAttribute("cart", cart);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/cart/update")
    public ResponseEntity<List<CartItemDto>> updateCartItem(@RequestParam int index, @RequestParam int delta, HttpSession session) {
        List<CartItemDto> cart = getCart(session);
        if (index >= 0 && index < cart.size()) {
            CartItemDto item = cart.get(index);
            item.setQty(item.getQty() + delta);
            if (item.getQty() <= 0) {
                cart.remove(index);
            }
        }
        session.setAttribute("cart", cart);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Long>> getFavoriteItems(HttpSession session) {
        return ResponseEntity.ok(getFavorites(session));
    }

    @PostMapping("/favorites/toggle")
    public ResponseEntity<List<Long>> toggleFavorite(@RequestParam Long id, HttpSession session) {
        List<Long> favs = getFavorites(session);
        if (favs.contains(id)) {
            favs.remove(id);
        } else {
            favs.add(id);
        }
        session.setAttribute("favorites", favs);
        return ResponseEntity.ok(favs);
    }
}
