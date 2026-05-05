package com.school.ecommerce.service;

import com.school.ecommerce.model.Creator;
import com.school.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MockDataService {

    public List<Product> getProductos() {
        return Arrays.asList(
            new Product("Blazer Overcut", "Nude Studio", "$2,400", "chaqueta.jpg"),
            new Product("Pantalón Parachute", "Vntg Archive", "$1,850", "cooljeans.jpg"),
            new Product("Bolso Puffer", "Maison O", "$1,200", "faldasvarias.jpg"),
            new Product("Vaso Asimétrico", "Raw Ceramics", "$650", "lentes.png"),
            new Product("Camisa Lino Crudo", "La Mercería", "$1,450", "aretes.jpg")
        );
    }

    public List<Creator> getCreadores() {
        return Arrays.asList(
            new Creator("Daniela's Archive", "Ciudad de México", 42, Arrays.asList("Vintage", "Y2K"),
                    "D", "", "", "", ""),
            new Creator("Minimalist Edit", "Guadalajara", 18, Arrays.asList("Basics", "Linen"),
                    "M", "linear-gradient(135deg, #E8DCCB, #D8CAB8)", "#DFD0E8", "#EBCFC0", "#E8DCCB"),
            new Creator("Objetos Vivos", "Monterrey", 56, Arrays.asList("Home", "Ceramics"),
                    "O", "linear-gradient(135deg, #F0E8F5, #DFD0E8)", "#EBCFC0", "#E8DCCB", "#DFD0E8")
        );
    }
}
