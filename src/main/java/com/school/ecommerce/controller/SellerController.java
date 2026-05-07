package com.school.ecommerce.controller;

import com.school.ecommerce.model.Product;
import com.school.ecommerce.model.ProductCollection;
import com.school.ecommerce.model.ProductStatus;
import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.User;
import com.school.ecommerce.repository.ProductCollectionRepository;
import com.school.ecommerce.repository.ProductRepository;
import com.school.ecommerce.repository.UserRepository;
import com.school.ecommerce.service.CloudinaryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vender")
public class SellerController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductCollectionRepository productCollectionRepository;
    private final CloudinaryService cloudinaryService;

    public SellerController(UserRepository userRepository,
                            ProductRepository productRepository,
                            ProductCollectionRepository productCollectionRepository,
                            CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productCollectionRepository = productCollectionRepository;
        this.cloudinaryService = cloudinaryService;
    }

    /** GET /vender — muestra el hub o la página de crear perfil */
    @Transactional(readOnly = true)
    @GetMapping
    public String sellerHub(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        User currentUser = getUser(authentication);
        if (currentUser == null) return "redirect:/login";
        if (currentUser.getRole() == Role.ADMIN) return "redirect:/admin";

        model.addAttribute("user", currentUser);

        // Si ya es SELLER muestra su catálogo
        if (currentUser.getRole() == Role.SELLER) {
            List<Product> misProductos = productRepository.findBySeller(currentUser);
            List<ProductCollection> misColecciones = productCollectionRepository.findBySellerOrderByIdDesc(currentUser);
            model.addAttribute("myProducts", misProductos);
            model.addAttribute("myCollections", misColecciones);
            model.addAttribute("statusCount", Map.of(
                    ProductStatus.AVAILABLE, misProductos.stream().filter(p -> p.getStatus() == ProductStatus.AVAILABLE).count(),
                    ProductStatus.PENDING, misProductos.stream().filter(p -> p.getStatus() == ProductStatus.PENDING).count(),
                    ProductStatus.SOLD, misProductos.stream().filter(p -> p.getStatus() == ProductStatus.SOLD).count()
            ));
            return "seller-hub";
        }

        // Si es BUYER y aún no tiene tienda, muestra el formulario de crear perfil
        return "create-profile";
    }

    /** POST /vender/crear-perfil — convierte al usuario en SELLER */
    @PostMapping("/crear-perfil")
    public String createProfile(Authentication authentication,
                                @RequestParam String location,
                                @RequestParam(required = false) String storeName,
                                @RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String tags,
                                @RequestParam(required = false) String avatarGradient) {
        if (authentication == null) return "redirect:/login";

        User currentUser = getUser(authentication);
        if (currentUser == null) return "redirect:/login";

        // Actualizar nombre si se proporcionó
        if (firstName != null && !firstName.isBlank()) currentUser.setFirstName(firstName.trim());
        if (lastName != null && !lastName.isBlank()) currentUser.setLastName(lastName.trim());

        if (location == null || location.isBlank()) {
            return "redirect:/vender?error=location-required";
        }

        if (storeName != null && !storeName.isBlank()) {
            currentUser.setUsername(resolveUniqueStoreName(storeName.trim(), currentUser));
        }

        // Convertir a SELLER y guardar datos del perfil
        currentUser.setRole(Role.SELLER);
        currentUser.setLocation(location.trim());

        if (tags != null && !tags.isBlank()) {
            List<String> normalizedTags = Arrays.stream(tags.split("\\s*,\\s*"))
                    .map(String::trim)
                    .filter(tag -> !tag.isBlank())
                    .distinct()
                    .collect(Collectors.toList());
            currentUser.setTags(normalizedTags);
        } else {
            currentUser.setTags(List.of());
        }

        // Avatar inicial: primera letra del nombre o nombre de tienda
        String initial = currentUser.getFirstName() != null && !currentUser.getFirstName().isEmpty()
                ? currentUser.getFirstName().substring(0, 1).toUpperCase() : "U";
        if ("U".equals(initial) && currentUser.getUsername() != null && !currentUser.getUsername().isBlank()) {
            initial = currentUser.getUsername().substring(0, 1).toUpperCase();
        }
        currentUser.setAvatarInitial(initial);

        // Gradiente del avatar
        String gradient = (avatarGradient != null && !avatarGradient.isBlank())
                ? avatarGradient
                : "linear-gradient(135deg, #F0E8F5, #DFD0E8)";
        currentUser.setAvatarGradient(gradient);

        // Paleta de colores por defecto para la tarjeta de creador
        currentUser.setMainBg("#EBCFC0");
        currentUser.setSub1Bg("#E8DCCB");
        currentUser.setSub2Bg("#DFD0E8");

        userRepository.save(currentUser);

        return "redirect:/vender";
    }

    /** GET /vender/subir-producto — formulario de subida */
    @Transactional(readOnly = true)
    @GetMapping("/subir-producto")
    public String uploadProductForm(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";

        User currentUser = getUser(authentication);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) {
            return "redirect:/vender";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("collections", productCollectionRepository.findBySellerOrderByIdDesc(currentUser));
        return "upload-product";
    }

    @PostMapping("/colecciones")
    public String createCollection(Authentication authentication,
                                   @RequestParam("name") String name,
                                   @RequestParam(value = "description", required = false) String description) {
        if (authentication == null) return "redirect:/login";
        User currentUser = getUser(authentication);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) return "redirect:/vender";

        if (name == null || name.isBlank()) {
            return "redirect:/vender?error=collection-name";
        }

        ProductCollection collection = new ProductCollection();
        collection.setName(name.trim());
        collection.setDescription(description != null && !description.isBlank() ? description.trim() : null);
        collection.setSeller(currentUser);
        productCollectionRepository.save(collection);

        return "redirect:/vender?collectionCreated=true";
    }

    /** POST /vender/subir-producto — guarda producto con imagen en Cloudinary */
    @PostMapping("/subir-producto")
    public String handleProductUpload(Authentication authentication,
                                      @RequestParam("name") String name,
                                      @RequestParam("price") String price,
                                      @RequestParam("description") String description,
                                      @RequestParam(value = "stock", defaultValue = "1") Integer stock,
                                      @RequestParam(value = "sizes", required = false) List<String> sizes,
                                      @RequestParam(value = "collectionId", required = false) Long collectionId,
                                      @RequestParam(value = "image", required = false) MultipartFile file) {
        if (authentication == null) return "redirect:/login";

        User currentUser = getUser(authentication);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) return "redirect:/vender";

        if (file == null || file.isEmpty()) {
            return "redirect:/vender/subir-producto?error=image-required";
        }

        // Subir imagen a Cloudinary
        String imageUrl;
        try {
            imageUrl = cloudinaryService.uploadImage(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/vender/subir-producto?error=image-upload";
        }

        // Parsear precio
        BigDecimal parsedPrice;
        try {
            parsedPrice = new BigDecimal(price.replace(",", "").replace("$", "").trim());
        } catch (NumberFormatException e) {
            parsedPrice = BigDecimal.ZERO;
        }

        Product product = new Product();
        product.setName(name != null ? name.trim() : "");
        String brand;
        if (currentUser.getFirstName() != null && !currentUser.getFirstName().isBlank()) {
            brand = currentUser.getFirstName().trim()
                    + (currentUser.getLastName() != null && !currentUser.getLastName().isBlank()
                    ? " " + currentUser.getLastName().trim() : "");
        } else {
            brand = currentUser.getUsername();
        }
        product.setBrand(brand);
        product.setPrice(parsedPrice);
        product.setDescription(description != null ? description.trim() : null);
        product.setStock(stock != null && stock > 0 ? stock : 1);
        product.setSizes(normalizeSizes(sizes));
        product.setImage(imageUrl);
        product.setStatus(ProductStatus.AVAILABLE);
        product.setSeller(currentUser);
        ProductCollection collection = findOwnedCollection(collectionId, currentUser);
        if (collection != null) {
            product.setCollection(collection);
        }

        productRepository.save(product);

        return "redirect:/vender?success=true";
    }

    @PostMapping("/producto/{id}/estado")
    public String updateProductStatus(Authentication authentication,
                                      @PathVariable Long id,
                                      @RequestParam("status") ProductStatus status) {
        if (authentication == null) return "redirect:/login";
        User currentUser = getUser(authentication);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) return "redirect:/vender";

        Product product = productRepository.findById(id).orElse(null);
        if (product == null || product.getSeller() == null || !product.getSeller().getId().equals(currentUser.getId())) {
            return "redirect:/vender?error=not-allowed";
        }

        product.setStatus(status);
        productRepository.save(product);
        return "redirect:/vender?updated=true";
    }

    @PostMapping("/producto/{id}/coleccion")
    public String updateProductCollection(Authentication authentication,
                                          @PathVariable Long id,
                                          @RequestParam(value = "collectionId", required = false) Long collectionId) {
        if (authentication == null) return "redirect:/login";
        User currentUser = getUser(authentication);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) return "redirect:/vender";

        Product product = productRepository.findById(id).orElse(null);
        if (product == null || product.getSeller() == null || !product.getSeller().getId().equals(currentUser.getId())) {
            return "redirect:/vender?error=not-allowed";
        }

        product.setCollection(findOwnedCollection(collectionId, currentUser));
        productRepository.save(product);
        return "redirect:/vender?collectionUpdated=true";
    }

    @PostMapping("/producto/{id}/eliminar")
    public String deleteProduct(Authentication authentication, @PathVariable Long id) {
        if (authentication == null) return "redirect:/login";
        User currentUser = getUser(authentication);
        if (currentUser == null || currentUser.getRole() != Role.SELLER) return "redirect:/vender";

        Product product = productRepository.findById(id).orElse(null);
        if (product == null || product.getSeller() == null || !product.getSeller().getId().equals(currentUser.getId())) {
            return "redirect:/vender?error=not-allowed";
        }

        productRepository.delete(product);
        return "redirect:/vender?deleted=true";
    }

    // ── helper ──────────────────────────────────────────────────────────────
    private User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }

    private String resolveUniqueStoreName(String requestedName, User currentUser) {
        String candidate = requestedName;
        int suffix = 2;
        while (true) {
            User existing = userRepository.findByUsername(candidate).orElse(null);
            if (existing == null || existing.getId().equals(currentUser.getId())) {
                return candidate;
            }
            candidate = requestedName + " " + suffix;
            suffix++;
        }
    }

    private List<String> normalizeSizes(List<String> sizes) {
        if (sizes == null) return List.of();
        return sizes.stream()
                .map(String::trim)
                .filter(size -> !size.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

    private ProductCollection findOwnedCollection(Long collectionId, User currentUser) {
        if (collectionId == null) return null;
        return productCollectionRepository.findById(collectionId)
                .filter(collection -> collection.getSeller() != null
                        && collection.getSeller().getId().equals(currentUser.getId()))
                .orElse(null);
    }
}
