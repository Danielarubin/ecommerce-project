package com.school.ecommerce.controller;

import com.school.ecommerce.model.Role;
import com.school.ecommerce.repository.ProductCollectionRepository;
import com.school.ecommerce.repository.ProductRepository;
import com.school.ecommerce.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Controlador para las páginas de exploración pública:
 *   /colecciones  - catálogo de todos los productos
 *   /creadores    - lista de usuarios con rol SELLER
 *   /tiendas      - alias de /creadores (vista alternativa)
 *   /favoritos    - favoritos guardados en sesión
 */
@Controller
public class ExploreController {

    private final ProductRepository productRepository;
    private final ProductCollectionRepository productCollectionRepository;
    private final UserRepository userRepository;

    public ExploreController(ProductRepository productRepository,
                             ProductCollectionRepository productCollectionRepository,
                             UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productCollectionRepository = productCollectionRepository;
        this.userRepository = userRepository;
    }

    /** GET /colecciones — colecciones creadas por vendedores */
    @Transactional(readOnly = true)
    @GetMapping("/colecciones")
    public String colecciones(Model model) {
        var collections = productCollectionRepository.findBySellerIsNotNullOrderByIdDesc();
        model.addAttribute("collections", collections);
        model.addAttribute("productos", productRepository.findBySellerIsNotNullOrderByIdDesc());
        return "collections";
    }

    /** GET /colecciones/{id} — detalle de una colección */
    @Transactional(readOnly = true)
    @GetMapping("/colecciones/{id}")
    public String coleccionDetalle(@PathVariable Long id, Model model) {
        var collectionOpt = productCollectionRepository.findById(id);
        if (collectionOpt.isEmpty() || collectionOpt.get().getSeller() == null) {
            return "redirect:/colecciones";
        }

        var collection = collectionOpt.get();
        model.addAttribute("collection", collection);
        model.addAttribute("productos", productRepository.findByCollectionIdOrderByIdDesc(collection.getId()));
        return "collection-detail";
    }

    /** GET /creadores — lista de vendedores/creadores */
    @Transactional(readOnly = true)
    @GetMapping("/creadores")
    public String creadores(Model model) {
        var creadores = userRepository.findByRole(Role.SELLER);
        long tiendasConPiezas = creadores.stream()
                .filter(creador -> creador.getProducts() != null && !creador.getProducts().isEmpty())
                .count();
        model.addAttribute("creadores", creadores);
        model.addAttribute("tiendasConPiezas", tiendasConPiezas);
        return "creators";
    }

    /** GET /tiendas — alias de /creadores */
    @Transactional(readOnly = true)
    @GetMapping("/tiendas")
    public String tiendas(Model model) {
        model.addAttribute("creadores", userRepository.findByRole(Role.SELLER));
        return "stores";
    }

    /** GET /tienda/{id} — escaparate público de una tienda */
    @Transactional(readOnly = true)
    @GetMapping("/tienda/{id}")
    public String tiendaDetalle(@PathVariable Long id, Model model) {
        var sellerOpt = userRepository.findById(id);
        if (sellerOpt.isEmpty() || sellerOpt.get().getRole() != Role.SELLER) {
            return "redirect:/tiendas";
        }

        var seller = sellerOpt.get();
        model.addAttribute("tienda", seller);
        model.addAttribute("collections", productCollectionRepository.findBySellerIdOrderByIdDesc(seller.getId()));
        model.addAttribute("productos", productRepository.findBySellerIdOrderByIdDesc(seller.getId()));
        return "store-detail";
    }

    /** GET /favoritos — favoritos guardados en la sesión del browser */
    @GetMapping("/favoritos")
    public String favoritos(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Long> favoriteIds = (List<Long>) session.getAttribute("favorites");
        if (favoriteIds != null && !favoriteIds.isEmpty()) {
            model.addAttribute("favoriteProducts", productRepository.findAllById(favoriteIds));
        }
        return "favorites";
    }
}
