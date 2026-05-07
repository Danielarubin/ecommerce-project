package com.school.ecommerce.repository;

import com.school.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    java.util.List<Product> findBySeller(com.school.ecommerce.model.User seller);
    java.util.List<Product> findBySellerIdOrderByIdDesc(Long sellerId);
    java.util.List<Product> findByCollectionIdOrderByIdDesc(Long collectionId);
    java.util.List<Product> findBySellerIsNotNullOrderByIdDesc();
    java.util.List<Product> findByStatusIsNull();
    long countBySellerId(Long sellerId);
    long deleteBySellerIsNull();
}
