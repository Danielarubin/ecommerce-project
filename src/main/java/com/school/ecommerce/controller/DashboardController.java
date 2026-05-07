package com.school.ecommerce.controller;

import com.school.ecommerce.model.Product;
import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.User;
import com.school.ecommerce.repository.ProductRepository;
import com.school.ecommerce.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public DashboardController(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public String showDashboard(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElse(null);

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);

        if (currentUser.getRole() == Role.SELLER) {
            model.addAttribute("misProductos", productRepository.findBySeller(currentUser));
            model.addAttribute("newProduct", new Product());
        } else {
            model.addAttribute("misOrdenes", currentUser.getOrders());
        }
        
        return "dashboard";
    }

    @PostMapping("/producto/nuevo")
    public String addProduct(@ModelAttribute("newProduct") Product newProduct, Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        newProduct.setSeller(currentUser);
        // Si no suben imagen, ponemos una por defecto para no romper el front
        if (newProduct.getImage() == null || newProduct.getImage().trim().isEmpty()) {
            newProduct.setImage("default-product.jpg");
        }
        
        productRepository.save(newProduct);

        return "redirect:/dashboard?success=true";
    }
}
