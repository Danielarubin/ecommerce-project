package com.school.ecommerce.service;

import com.school.ecommerce.dto.CartItemDto;
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
import java.util.List;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserRepository userRepository;

    public CheckoutService(ProductRepository productRepository, 
                           PurchaseOrderRepository purchaseOrderRepository, 
                           UserRepository userRepository) {
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void processCheckout(List<CartItemDto> cartItems, String email) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado en la base de datos"));

        PurchaseOrder order = new PurchaseOrder();
        order.setBuyer(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("COMPLETED");

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItemDto item : cartItems) {
            Product product = productRepository.findById(Long.parseLong(item.getId()))
                    .orElseThrow(() -> new IllegalStateException("Producto no encontrado con ID: " + item.getId()));

            if (product.getStock() < item.getQty()) {
                throw new IllegalStateException("No hay suficiente stock para el producto: " + product.getName() + ". Stock disponible: " + product.getStock());
            }

            // Reducir stock
            product.setStock(product.getStock() - item.getQty());

            // Crear el OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQty());
            orderItem.setUnitPrice(BigDecimal.valueOf(item.getPrice()));

            // Añadir el item a la orden y sumar al total
            order.getItems().add(orderItem);
            totalAmount = totalAmount.add(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }

        order.setTotalAmount(totalAmount);
        
        // Guardar la orden (por CascadeType.ALL se guardarán los OrderItems también)
        // Y al estar dentro de @Transactional, los cambios en el Product (stock) se reflejarán en BD
        purchaseOrderRepository.save(order);
    }
}
