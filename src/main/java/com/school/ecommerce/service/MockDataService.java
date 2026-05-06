package com.school.ecommerce.service;

import com.school.ecommerce.model.Creator;
import com.school.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    }
}
