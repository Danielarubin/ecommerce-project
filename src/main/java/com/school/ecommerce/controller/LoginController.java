package com.school.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        if (email != null && !email.trim().isEmpty() && password != null && !password.isEmpty()) {
            // Mock login successful
            String username = email.split("@")[0];
            session.setAttribute("user", username);
            return "redirect:/";
        }
        
        model.addAttribute("error", "Por favor ingresa credenciales válidas.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String nombre, 
                           @RequestParam String apellido, 
                           @RequestParam String email, 
                           @RequestParam String telefono, 
                           @RequestParam String password, 
                           HttpSession session, 
                           Model model) {
        if (nombre != null && !nombre.trim().isEmpty() &&
            apellido != null && !apellido.trim().isEmpty() &&
            email != null && !email.trim().isEmpty() &&
            telefono != null && !telefono.trim().isEmpty() &&
            password != null && !password.isEmpty()) {
            
            // Mock register successful
            session.setAttribute("user", nombre);
            return "redirect:/";
        }
        
        model.addAttribute("error", "Por favor completa todos los campos requeridos.");
        return "register";
    }
}
