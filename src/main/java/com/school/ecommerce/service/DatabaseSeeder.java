package com.school.ecommerce.service;

import com.school.ecommerce.model.Product;
import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.User;
import com.school.ecommerce.repository.ProductRepository;
import com.school.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("=============================================");
            System.out.println("Sembrando datos de prueba en PostgreSQL...");
            System.out.println("=============================================");

            User u1 = new User(); u1.setUsername("Daniela's Archive"); u1.setLocation("Ciudad de México"); u1.setTags(Arrays.asList("Vintage", "Y2K")); u1.setAvatarInitial("D"); u1.setRole(Role.SELLER); u1.setEmail("d@test.com"); u1.setPassword(passwordEncoder.encode("123"));
            User u2 = new User(); u2.setUsername("Minimalist Edit"); u2.setLocation("Guadalajara"); u2.setTags(Arrays.asList("Basics", "Linen")); u2.setAvatarInitial("M"); u2.setAvatarGradient("linear-gradient(135deg, #E8DCCB, #D8CAB8)"); u2.setMainBg("#DFD0E8"); u2.setSub1Bg("#EBCFC0"); u2.setSub2Bg("#E8DCCB"); u2.setRole(Role.SELLER); u2.setEmail("m@test.com"); u2.setPassword(passwordEncoder.encode("123"));
            User u3 = new User(); u3.setUsername("Objetos Vivos"); u3.setLocation("Monterrey"); u3.setTags(Arrays.asList("Home", "Ceramics")); u3.setAvatarInitial("O"); u3.setAvatarGradient("linear-gradient(135deg, #F0E8F5, #DFD0E8)"); u3.setMainBg("#EBCFC0"); u3.setSub1Bg("#E8DCCB"); u3.setSub2Bg("#DFD0E8"); u3.setRole(Role.SELLER); u3.setEmail("o@test.com"); u3.setPassword(passwordEncoder.encode("123"));

            userRepository.saveAll(Arrays.asList(u1, u2, u3));

            Product p1 = new Product(); p1.setName("Blazer Overcut"); p1.setBrand("Nude Studio"); p1.setPrice(new BigDecimal("2400.00")); p1.setImage("chaqueta.jpg"); p1.setSeller(u1); p1.setStock(10);
            Product p2 = new Product(); p2.setName("Pantalón Parachute"); p2.setBrand("Vntg Archive"); p2.setPrice(new BigDecimal("1850.00")); p2.setImage("cooljeans.jpg"); p2.setSeller(u1); p2.setStock(5);
            Product p3 = new Product(); p3.setName("Bolso Puffer"); p3.setBrand("Maison O"); p3.setPrice(new BigDecimal("1200.00")); p3.setImage("faldasvarias.jpg"); p3.setSeller(u2); p3.setStock(8);
            Product p4 = new Product(); p4.setName("Vaso Asimétrico"); p4.setBrand("Raw Ceramics"); p4.setPrice(new BigDecimal("650.00")); p4.setImage("lentes.png"); p4.setSeller(u3); p4.setStock(20);
            Product p5 = new Product(); p5.setName("Camisa Lino Crudo"); p5.setBrand("La Mercería"); p5.setPrice(new BigDecimal("1450.00")); p5.setImage("aretes.jpg"); p5.setSeller(u2); p5.setStock(12);

            productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

            System.out.println("Base de datos inicializada exitosamente.");
        }
    }
}
