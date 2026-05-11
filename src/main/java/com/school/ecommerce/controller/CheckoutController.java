package com.school.ecommerce.controller;

import com.school.ecommerce.dto.CartItemDto;
import com.school.ecommerce.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

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

    @SuppressWarnings("unchecked")
    @PostMapping("/checkout")
    public String processCheckout(HttpSession session, RedirectAttributes redirectAttributes, Principal principal) {
        if (principal == null) {
            // Guarda el carrito en sesión y redirige al login con indicación de retorno
            redirectAttributes.addFlashAttribute("loginRequired",
                    "Necesitas iniciar sesión para completar tu compra. Tu bolsa se ha guardado.");
            return "redirect:/login?from=checkout";
        }
        String username = principal.getName();

        List<CartItemDto> cart = (List<CartItemDto>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/";
        }

        try {
            checkoutService.processCheckout(cart, username);
            session.removeAttribute("cart");
            return "redirect:/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "success";
    }
}
