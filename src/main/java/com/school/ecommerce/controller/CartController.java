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
import java.util.Objects;
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
    public String addToCart(@PathVariable Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @RequestParam(value = "selectedSize", required = false) String selectedSize,
                            HttpSession session) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            String normalizedSize = normalizeSize(selectedSize);
            
            List<CartItemDto> cart = (List<CartItemDto>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }
            
            boolean found = false;
            for (CartItemDto item : cart) {
                if (item.getId().equals(product.getId()) && Objects.equals(item.getSelectedSize(), normalizedSize)) {
                    item.setQty(item.getQty() + quantity);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                CartItemDto newItem = new CartItemDto();
                newItem.setId(product.getId());
                newItem.setName(product.getName());
                newItem.setBrand(product.getBrand());
                newItem.setPrice(product.getPrice().doubleValue());
                newItem.setImage(product.getImage());
                newItem.setSelectedSize(normalizedSize);
                newItem.setQty(quantity);
                cart.add(newItem);
            }
            
            session.setAttribute("cart", cart);
        }
        
        return "redirect:/";
    }

    private String normalizeSize(String selectedSize) {
        return selectedSize == null || selectedSize.isBlank() ? null : selectedSize.trim();
    }
}
