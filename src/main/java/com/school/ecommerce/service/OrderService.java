package com.school.ecommerce.service;

import com.school.ecommerce.dto.OrderItemRequest;
import com.school.ecommerce.dto.OrderRequest;
import com.school.ecommerce.model.OrderItem;
import com.school.ecommerce.model.Product;
import com.school.ecommerce.model.PurchaseOrder;
import com.school.ecommerce.model.User;
import com.school.ecommerce.repository.ProductRepository;
import com.school.ecommerce.repository.PurchaseOrderRepository;
import com.school.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(PurchaseOrderRepository purchaseOrderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PurchaseOrder createOrder(OrderRequest orderRequest) {
        User buyer = userRepository.findById(orderRequest.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + orderRequest.getBuyerId()));

        PurchaseOrder order = new PurchaseOrder();
        order.setBuyer(buyer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // Deduct stock
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            // Calculate amount
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            order.getItems().add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        
        return purchaseOrderRepository.save(order);
    }
}
