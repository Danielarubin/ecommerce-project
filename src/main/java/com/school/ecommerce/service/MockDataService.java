package com.school.ecommerce.service;

import com.school.ecommerce.model.Product;
import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MockDataService {

    private List<Product> productos;
    private long currentId = 1L;

    public MockDataService() {

        productos = new ArrayList<>(Arrays.asList(

                createProduct(
                        "Blazer Overcut",
                        "Nude Studio",
                        "2400.00",
                        "chaqueta.jpg"),

                createProduct(
                        "Pantalón Parachute",
                        "Vntg Archive",
                        "1850.00",
                        "cooljeans.jpg"),

                createProduct(
                        "Bolso Puffer",
                        "Maison O",
                        "1200.00",
                        "faldasvarias.jpg"),

                createProduct(
                        "Vaso Asimétrico",
                        "Raw Ceramics",
                        "650.00",
                        "lentes.png"),

                createProduct(
                        "Camisa Lino Crudo",
                        "La Mercería",
                        "1450.00",
                        "aretes.jpg")));
    }

    private Product createProduct(
            String name,
            String brand,
            String price,
            String image) {

        Product p = new Product();
        p.setId(currentId++);
        p.setName(name);
        p.setBrand(brand);
        p.setPrice(new BigDecimal(price));
        p.setImage(image);

        return p;
    }

    public List<Product> getProductos() {
        return productos;
    }

    public List<Product> getProductsByIds(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        return productos.stream()
                .filter(p -> ids.contains(p.getId()))
                .toList();
    }

    public void addProduct(Product p) {
        if (p.getId() == null) {
            p.setId(currentId++);
        }
        productos.add(0, p);
    }

    public List<User> getCreadores() {

        User u1 = new User();
        u1.setUsername("Daniela's Archive");
        u1.setLocation("Ciudad de México");
        u1.setTags(Arrays.asList("Vintage", "Y2K"));
        u1.setAvatarInitial("D");
        u1.setRole(Role.SELLER);
        u1.setEmail("d@test.com");
        u1.setPassword("123");

        for (int i = 0; i < 42; i++) {
            u1.getProducts().add(new Product());
        }

        User u2 = new User();
        u2.setUsername("Minimalist Edit");
        u2.setLocation("Guadalajara");
        u2.setTags(Arrays.asList("Basics", "Linen"));
        u2.setAvatarInitial("M");
        u2.setAvatarGradient("linear-gradient(135deg, #E8DCCB, #D8CAB8)");
        u2.setMainBg("#DFD0E8");
        u2.setSub1Bg("#EBCFC0");
        u2.setSub2Bg("#E8DCCB");
        u2.setRole(Role.SELLER);
        u2.setEmail("m@test.com");
        u2.setPassword("123");

        for (int i = 0; i < 18; i++) {
            u2.getProducts().add(new Product());
        }

        User u3 = new User();
        u3.setUsername("Objetos Vivos");
        u3.setLocation("Monterrey");
        u3.setTags(Arrays.asList("Home", "Ceramics"));
        u3.setAvatarInitial("O");
        u3.setAvatarGradient("linear-gradient(135deg, #F0E8F5, #DFD0E8)");
        u3.setMainBg("#EBCFC0");
        u3.setSub1Bg("#E8DCCB");
        u3.setSub2Bg("#DFD0E8");
        u3.setRole(Role.SELLER);
        u3.setEmail("o@test.com");
        u3.setPassword("123");

        for (int i = 0; i < 56; i++) {
            u3.getProducts().add(new Product());
        }

        return Arrays.asList(u1, u2, u3);
    }
}