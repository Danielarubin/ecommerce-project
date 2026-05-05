package com.school.ecommerce.controller;

import com.school.ecommerce.service.MockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
