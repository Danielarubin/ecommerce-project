package com.school.ecommerce.service;

import com.school.ecommerce.model.Product;
import com.school.ecommerce.model.ProductStatus;
import com.school.ecommerce.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DatabaseSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<Product> productsWithoutStatus = productRepository.findByStatusIsNull();
        if (!productsWithoutStatus.isEmpty()) {
            productsWithoutStatus.forEach(product -> product.setStatus(ProductStatus.AVAILABLE));
            productRepository.saveAll(productsWithoutStatus);
        }
    }
}
