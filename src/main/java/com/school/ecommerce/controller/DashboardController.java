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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.school.ecommerce.service.CloudinaryService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    public DashboardController(UserRepository userRepository, ProductRepository productRepository, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cloudinaryService = cloudinaryService;
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
    public String addProduct(@ModelAttribute("newProduct") Product newProduct, 
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        newProduct.setSeller(currentUser);
        
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(imageFile);
                newProduct.setImage(imageUrl);
            } else if (newProduct.getImage() == null || newProduct.getImage().trim().isEmpty()) {
                newProduct.setImage("default-product.jpg");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/dashboard?error=image_upload_failed";
        }
        
        productRepository.save(newProduct);

        return "redirect:/dashboard?success=true";
    }
}
