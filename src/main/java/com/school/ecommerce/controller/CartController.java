package com.school.ecommerce.controller;

import com.school.ecommerce.dto.CartItemDto;
import com.school.ecommerce.model.Product;
import com.school.ecommerce.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            
            List<CartItemDto> cart = (List<CartItemDto>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }
            
            boolean found = false;
            for (CartItemDto item : cart) {
                if (item.getId().equals(String.valueOf(product.getId()))) {
                    item.setQty(item.getQty() + quantity);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                CartItemDto newItem = new CartItemDto();
                newItem.setId(String.valueOf(product.getId()));
                newItem.setName(product.getName());
                newItem.setBrand(product.getBrand());
                newItem.setPrice(product.getPrice().doubleValue());
                newItem.setImage(product.getImage());
                newItem.setQty(quantity);
                cart.add(newItem);
            }
            
            session.setAttribute("cart", cart);
        }
        
        return "redirect:/";
    }
}
