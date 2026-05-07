package com.school.ecommerce.repository;

import com.school.ecommerce.model.ProductCollection;
import com.school.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCollectionRepository extends JpaRepository<ProductCollection, Long> {
    List<ProductCollection> findBySellerOrderByIdDesc(User seller);
    List<ProductCollection> findBySellerIdOrderByIdDesc(Long sellerId);
    List<ProductCollection> findBySellerIsNotNullOrderByIdDesc();
    long countBySellerId(Long sellerId);
}
