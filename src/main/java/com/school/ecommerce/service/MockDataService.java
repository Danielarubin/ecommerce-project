package com.school.ecommerce.service;

import com.school.ecommerce.model.User;
import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class MockDataService {

    public List<Product> getProductos() {
        Product p1 = new Product(); p1.setName("Blazer Overcut"); p1.setBrand("Nude Studio"); p1.setPrice(new BigDecimal("2400.00")); p1.setImage("chaqueta.jpg");
        Product p2 = new Product(); p2.setName("Pantalón Parachute"); p2.setBrand("Vntg Archive"); p2.setPrice(new BigDecimal("1850.00")); p2.setImage("cooljeans.jpg");
        Product p3 = new Product(); p3.setName("Bolso Puffer"); p3.setBrand("Maison O"); p3.setPrice(new BigDecimal("1200.00")); p3.setImage("faldasvarias.jpg");
        Product p4 = new Product(); p4.setName("Vaso Asimétrico"); p4.setBrand("Raw Ceramics"); p4.setPrice(new BigDecimal("650.00")); p4.setImage("lentes.png");
        Product p5 = new Product(); p5.setName("Camisa Lino Crudo"); p5.setBrand("La Mercería"); p5.setPrice(new BigDecimal("1450.00")); p5.setImage("aretes.jpg");
        return Arrays.asList(p1, p2, p3, p4, p5);
    }

    public List<User> getCreadores() {
        User u1 = new User(); u1.setUsername("Daniela's Archive"); u1.setLocation("Ciudad de México"); u1.setTags(Arrays.asList("Vintage", "Y2K")); u1.setAvatarInitial("D"); u1.setRole(Role.SELLER); u1.setEmail("d@test.com"); u1.setPassword("123");
        for(int i=0; i<42; i++) u1.getProducts().add(new Product());
        
        User u2 = new User(); u2.setUsername("Minimalist Edit"); u2.setLocation("Guadalajara"); u2.setTags(Arrays.asList("Basics", "Linen")); u2.setAvatarInitial("M"); u2.setAvatarGradient("linear-gradient(135deg, #E8DCCB, #D8CAB8)"); u2.setMainBg("#DFD0E8"); u2.setSub1Bg("#EBCFC0"); u2.setSub2Bg("#E8DCCB"); u2.setRole(Role.SELLER); u2.setEmail("m@test.com"); u2.setPassword("123");
        for(int i=0; i<18; i++) u2.getProducts().add(new Product());

        User u3 = new User(); u3.setUsername("Objetos Vivos"); u3.setLocation("Monterrey"); u3.setTags(Arrays.asList("Home", "Ceramics")); u3.setAvatarInitial("O"); u3.setAvatarGradient("linear-gradient(135deg, #F0E8F5, #DFD0E8)"); u3.setMainBg("#EBCFC0"); u3.setSub1Bg("#E8DCCB"); u3.setSub2Bg("#DFD0E8"); u3.setRole(Role.SELLER); u3.setEmail("o@test.com"); u3.setPassword("123");
        for(int i=0; i<56; i++) u3.getProducts().add(new Product());

        return Arrays.asList(u1, u2, u3);
    }
}
