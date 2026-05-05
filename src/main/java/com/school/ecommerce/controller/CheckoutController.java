package com.school.ecommerce.controller;

import com.school.ecommerce.dto.CartItemDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CheckoutController {

    @SuppressWarnings("unchecked")
    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        List<CartItemDto> cart = (List<CartItemDto>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/"; // Redirect to home if cart is empty
        }
        
        double total = 0;
        for (CartItemDto item : cart) {
            total += item.getPrice() * item.getQty();
        }
        
        model.addAttribute("cartItems", cart);
        model.addAttribute("cartTotal", total);
        
        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(HttpSession session) {
        // Here we would typically save the order to the database
        // For now, we just clear the cart and show success page
        session.removeAttribute("cart");
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "success";
    }
}
