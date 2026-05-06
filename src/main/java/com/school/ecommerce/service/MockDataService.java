package com.school.ecommerce.service;

import com.school.ecommerce.model.User;
import com.school.ecommerce.model.Role;
import com.school.ecommerce.model.Product;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.math.BigDecimal;
>>>>>>> 71b1f6b251ed4772c6fe4b0f784e6090740a6b5f
import java.util.Arrays;
import java.util.List;

@Service
public class MockDataService {

    private List<Product> productos;
    private List<Creator> creadores;

    public MockDataService() {
        productos = new ArrayList<>(Arrays.asList(
            new Product("prod-1", "Blazer Overcut", "Nude Studio", "$2,400", "Blazer premium de corte oversized.", "lentejuelas.jpg"),
            new Product("prod-2", "Faldas Colección", "VNTG Archive", "$1,850", "Colección vintage de faldas.", "faldasvarias.jpg"),
            new Product("prod-3", "Denim Editado", "Maison O", "$1,200", "Pantalón de mezclilla con diseño único.", "cooljeans.jpg"),
            new Product("prod-4", "Pulseras Artesanales", "Orígenes", "$680", "Juego de pulseras hechas a mano.", "maspulseras.jpg"),
            new Product("prod-5", "Aretes Statement", "Taller Joya", "$420", "Aretes dorados llamativos.", "aretes.jpg"),
            new Product("prod-6", "Collar Artesanal", "Joyería Nativa", "$560", "Collar tejido con detalles naturales.", "collar.jpg"),
            new Product("prod-7", "Chanclas Trenzadas", "Artesana Piel", "$890", "Calzado cómodo de piel trenzada.", "chanclas.jpg"),
            new Product("prod-8", "Chaqueta Estructurada", "Estudio Local", "$3,200", "Chaqueta formal estructurada.", "chaqueta.jpg"),
            new Product("prod-9", "ChamarraLV Azul", "Louis Vuitton", "$3,200", "Chamarra exclusiva en tono azul.", "azul.jpg")
        ));

        creadores = new ArrayList<>(Arrays.asList(
            new Creator("Daniela's Archive", "daniela", "Ciudad de México", 42, Arrays.asList("Vintage", "Y2K"),
                    "D", "", "", "", ""),
            new Creator("Minimalist Edit", "minimalist", "Guadalajara", 18, Arrays.asList("Basics", "Linen"),
                    "M", "linear-gradient(135deg, #E8DCCB, #D8CAB8)", "#DFD0E8", "#EBCFC0", "#E8DCCB"),
            new Creator("Objetos Vivos", "objetosvivos", "Monterrey", 56, Arrays.asList("Home", "Ceramics"),
                    "O", "linear-gradient(135deg, #F0E8F5, #DFD0E8)", "#EBCFC0", "#E8DCCB", "#DFD0E8")
        ));
    }

    public List<Product> getProductos() {
<<<<<<< HEAD
        return productos;
    }

    public List<Product> getProductsByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return productos.stream()
                .filter(p -> ids.contains(p.getId()))
                .toList();
    }

    public void addProduct(Product p) {
        productos.add(0, p); // Add to the top of the list
    }

    public List<Creator> getCreadores() {
        return creadores;
    }

    public void addCreator(Creator c) {
        creadores.add(0, c);
=======
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
>>>>>>> 71b1f6b251ed4772c6fe4b0f784e6090740a6b5f
    }
}
