package com.school.ecommerce.controller;

import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.Product;
import com.school.ecommerce.repository.ProductRepository;
import com.school.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public HomeController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Leemos todos los productos desde PostgreSQL
        model.addAttribute("productos", productRepository.findAll());
        // Leemos a los usuarios que son vendedores para mostrarlos en el index
        model.addAttribute("creadores", userRepository.findByRole(Role.SELLER));
        return "index";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            model.addAttribute("producto", optionalProduct.get());
            return "producto";
        }
        return "redirect:/";
    }
}
