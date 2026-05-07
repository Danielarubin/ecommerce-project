package com.school.ecommerce.controller;

import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.Product;
import com.school.ecommerce.repository.ProductRepository;
import com.school.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@Controller
public class HomeController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public HomeController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @GetMapping("/")
    public String index(Model model) {
        // Solo productos asociados a una tienda real
        model.addAttribute("productos", productRepository.findBySellerIsNotNullOrderByIdDesc());
        // Leemos a los usuarios que son vendedores para mostrarlos en el index
        model.addAttribute("creadores", userRepository.findByRole(Role.SELLER));
        return "index";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent() && optionalProduct.get().getSeller() != null) {
            Product product = optionalProduct.get();
            model.addAttribute("producto", product);
            return "producto";
        }
        return "redirect:/";
    }
}
