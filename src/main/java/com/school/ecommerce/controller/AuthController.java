package com.school.ecommerce.controller;

import com.school.ecommerce.dto.UserRegistrationDto;
import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.User;
import com.school.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto dto, Model model) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "register";
        }

        User user = new User();
        user.setFirstName(dto.getNombre());
        user.setLastName(dto.getApellido());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getTelefono());
        
        // Asignamos el email como username por defecto para evitar errores de null,
        // o generamos uno combinando nombre y apellido.
        user.setUsername(dto.getNombre() + " " + dto.getApellido()); 
        
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.BUYER); // Por defecto un usuario nuevo es comprador

        userRepository.save(user);

        return "redirect:/login?registered=true";
    }
}
