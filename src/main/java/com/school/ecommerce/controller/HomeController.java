package com.school.ecommerce.controller;

import com.school.ecommerce.service.MockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final MockDataService mockDataService;

    @Autowired
    public HomeController(MockDataService mockDataService) {
        this.mockDataService = mockDataService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("productos", mockDataService.getProductos());
        model.addAttribute("creadores", mockDataService.getCreadores());
        return "index";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/favoritos")
    public String favoritos(HttpSession session, Model model) {
        List<String> favIds = (List<String>) session.getAttribute("favorites");
        if (favIds == null) {
            favIds = new ArrayList<>();
        }
        model.addAttribute("favoritos", mockDataService.getProductsByIds(favIds));
        return "favorites";
    }

    @GetMapping("/creadores")
    public String creadores(Model model) {
        model.addAttribute("creadores", mockDataService.getCreadores());
        return "creators";
    }

    @GetMapping("/tiendas")
    public String tiendas(Model model) {
        model.addAttribute("creadores", mockDataService.getCreadores());
        return "stores";
    }

    @GetMapping("/colecciones")
    public String colecciones(Model model) {
        // Simple collection grouping logic for the mock
        model.addAttribute("productos", mockDataService.getProductos());
        return "collections";
    }
}
