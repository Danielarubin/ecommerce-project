package com.school.ecommerce.controller;

import com.school.ecommerce.model.Product;
import com.school.ecommerce.model.ProductStatus;
import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.User;
import com.school.ecommerce.repository.OrderItemRepository;
import com.school.ecommerce.repository.ProductRepository;
import com.school.ecommerce.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public AdminController(UserRepository userRepository,
                           ProductRepository productRepository,
                           OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    @GetMapping
    public String dashboard(Model model) {
        List<User> sellers = userRepository.findByRole(Role.SELLER);
        model.addAttribute("sellerCount", sellers.size());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("recentProducts", productRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("recentSellers", sellers);
        return "admin/dashboard";
    }

    @Transactional(readOnly = true)
    @GetMapping("/creadores")
    public String creators(Model model) {
        model.addAttribute("users", userRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        return "admin/creators";
    }

    @Transactional(readOnly = true)
    @GetMapping("/creadores/{id}/editar")
    public String editCreator(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/admin/creadores?error=not-found";

        model.addAttribute("managedUser", user);
        model.addAttribute("roles", Role.values());
        return "admin/creator-form";
    }

    @Transactional
    @PostMapping("/creadores/{id}/editar")
    public String updateCreator(@PathVariable Long id,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String phone,
                                @RequestParam Role role,
                                @RequestParam(required = false) String location,
                                @RequestParam(required = false) String tags,
                                @RequestParam(required = false) String avatarInitial,
                                @RequestParam(required = false) String avatarGradient) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/admin/creadores?error=not-found";

        User emailOwner = userRepository.findByEmail(email.trim()).orElse(null);
        if (emailOwner != null && !emailOwner.getId().equals(user.getId())) {
            return "redirect:/admin/creadores/" + id + "/editar?error=email-exists";
        }

        User usernameOwner = userRepository.findByUsername(username.trim()).orElse(null);
        if (usernameOwner != null && !usernameOwner.getId().equals(user.getId())) {
            return "redirect:/admin/creadores/" + id + "/editar?error=username-exists";
        }

        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setFirstName(blankToNull(firstName));
        user.setLastName(blankToNull(lastName));
        user.setPhone(blankToNull(phone));
        user.setRole(role);
        user.setLocation(blankToNull(location));
        user.setAvatarInitial(blankToNull(avatarInitial));
        user.setAvatarGradient(blankToNull(avatarGradient));
        user.setTags(parseTags(tags));

        userRepository.save(user);
        if (role != Role.SELLER) {
            for (Product product : productRepository.findBySeller(user)) {
                removeProductFromCatalog(product);
            }
        }
        return "redirect:/admin/creadores?updated=true";
    }

    @Transactional
    @PostMapping("/creadores/{id}/eliminar")
    public String deleteCreator(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/admin/creadores?error=not-found";

        User currentAdmin = getCurrentAdmin(authentication);
        if (currentAdmin != null && currentAdmin.getId().equals(user.getId())) {
            return "redirect:/admin/creadores?error=self-delete";
        }

        if (user.getOrders() != null && !user.getOrders().isEmpty()) {
            return "redirect:/admin/creadores?error=user-has-orders";
        }

        for (Product product : productRepository.findBySeller(user)) {
            removeProductFromCatalog(product);
        }

        userRepository.delete(user);
        return "redirect:/admin/creadores?deleted=true";
    }

    @Transactional(readOnly = true)
    @GetMapping("/prendas")
    public String products(Model model) {
        model.addAttribute("products", productRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        return "admin/products";
    }

    @Transactional(readOnly = true)
    @GetMapping("/prendas/{id}/editar")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/admin/prendas?error=not-found";

        model.addAttribute("product", product);
        model.addAttribute("sellers", userRepository.findByRole(Role.SELLER));
        model.addAttribute("statuses", ProductStatus.values());
        return "admin/product-form";
    }

    @Transactional
    @PostMapping("/prendas/{id}/editar")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String brand,
                                @RequestParam String price,
                                @RequestParam(required = false) String description,
                                @RequestParam Integer stock,
                                @RequestParam ProductStatus status,
                                @RequestParam(required = false) String sizes,
                                @RequestParam(required = false) String image,
                                @RequestParam(required = false) Long sellerId) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/admin/prendas?error=not-found";

        product.setName(name.trim());
        product.setBrand(brand.trim());
        product.setPrice(parsePrice(price));
        product.setDescription(blankToNull(description));
        product.setStock(stock != null && stock >= 0 ? stock : 0);
        product.setSizes(parseTags(sizes));
        product.setStatus(status);
        product.setImage(blankToNull(image));

        User seller = null;
        if (sellerId != null) {
            seller = userRepository.findById(sellerId)
                    .filter(user -> user.getRole() == Role.SELLER)
                    .orElse(null);
        }
        product.setSeller(seller);

        productRepository.save(product);
        return "redirect:/admin/prendas?updated=true";
    }

    @Transactional
    @PostMapping("/prendas/{id}/eliminar")
    public String deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/admin/prendas?error=not-found";

        removeProductFromCatalog(product);
        return "redirect:/admin/prendas?deleted=true";
    }

    private void removeProductFromCatalog(Product product) {
        if (orderItemRepository.existsByProductId(product.getId())) {
            product.setSeller(null);
            product.setStock(0);
            product.setStatus(ProductStatus.SOLD);
            productRepository.save(product);
        } else {
            productRepository.delete(product);
        }
    }

    private User getCurrentAdmin(Authentication authentication) {
        if (authentication == null) return null;
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private List<String> parseTags(String tags) {
        if (tags == null || tags.isBlank()) return List.of();
        return Arrays.stream(tags.split("\\s*,\\s*"))
                .map(String::trim)
                .filter(tag -> !tag.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

    private BigDecimal parsePrice(String value) {
        try {
            return new BigDecimal(value.replace(",", "").replace("$", "").trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
