package com.school.ecommerce.controller;

import com.school.ecommerce.model.Creator;
import com.school.ecommerce.model.Product;
import com.school.ecommerce.service.MockDataService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Controller
@RequestMapping("/vender")
public class SellerController {

    private final String UPLOAD_DIR = "uploads/";

    @Autowired
    private MockDataService mockDataService;

    @GetMapping
    public String sellerHub(HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return "redirect:/login"; // Force login
        }
        
        // Find if user already has a store
        Creator userCreator = mockDataService.getCreadores().stream()
                .filter(c -> username.equals(c.getOwner()))
                .findFirst().orElse(null);

        model.addAttribute("creator", userCreator);
        
        if (userCreator == null) {
            return "create-profile";
        }
        
        // Return dashboard
        model.addAttribute("myProducts", mockDataService.getProductos().stream()
                .filter(p -> userCreator.getName().equals(p.getBrand()))
                .toList());
        return "seller-hub";
    }

    @PostMapping("/crear-perfil")
    public String createProfile(HttpSession session, 
                                @RequestParam String name, 
                                @RequestParam String location, 
                                @RequestParam String tags) {
        String username = (String) session.getAttribute("user");
        if (username == null) return "redirect:/login";

        Creator creator = new Creator(
                name,
                username,
                location,
                0,
                Arrays.asList(tags.split("\\s*,\\s*")),
                name.substring(0, 1).toUpperCase(),
                "linear-gradient(135deg, #F0E8F5, #DFD0E8)",
                "#EBCFC0",
                "#E8DCCB",
                "#DFD0E8"
        );
        mockDataService.addCreator(creator);
        
        return "redirect:/vender";
    }

    @GetMapping("/subir-producto")
    public String uploadProductForm(HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");
        if (username == null) return "redirect:/login";
        
        Creator userCreator = mockDataService.getCreadores().stream()
                .filter(c -> username.equals(c.getOwner()))
                .findFirst().orElse(null);

        if (userCreator == null) {
            return "redirect:/vender"; // must create profile first
        }
        
        return "upload-product";
    }

    @PostMapping("/subir-producto")
    public String handleFileUpload(HttpSession session,
                                   @RequestParam("name") String name,
                                   @RequestParam("price") String price,
                                   @RequestParam("description") String description,
                                   @RequestParam("image") MultipartFile file) {
        String username = (String) session.getAttribute("user");
        if (username == null) return "redirect:/login";

        Creator userCreator = mockDataService.getCreadores().stream()
                .filter(c -> username.equals(c.getOwner()))
                .findFirst().orElse(null);

        if (userCreator == null) return "redirect:/vender";

        String imageUrl = "";
        if (!file.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                // Set the URL path
                imageUrl = "/uploads/" + filename;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Product product = new Product(
                "prod-" + UUID.randomUUID().toString().substring(0, 8),
                name,
                userCreator.getName(), // Brand is the creator's name
                "$" + price,
                description,
                imageUrl
        );
        
        mockDataService.addProduct(product);
        userCreator.setNumberOfItems(userCreator.getNumberOfItems() + 1);

        return "redirect:/vender";
    }
}
