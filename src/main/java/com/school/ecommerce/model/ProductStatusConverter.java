package com.school.ecommerce.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProductStatusConverter implements AttributeConverter<ProductStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProductStatus status) {
        return status != null ? status.name() : ProductStatus.AVAILABLE.name();
    }

    @Override
    public ProductStatus convertToEntityAttribute(String value) {
        if (value == null || value.isBlank()) {
            return ProductStatus.AVAILABLE;
        }

        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "AVAILABLE", "DISPONIBLE", "1", "TRUE", "ACTIVO", "ACTIVE" -> ProductStatus.AVAILABLE;
            case "PENDING", "PENDIENTE", "0" -> ProductStatus.PENDING;
            case "SOLD", "VENDIDO", "2", "FALSE", "INACTIVO", "INACTIVE" -> ProductStatus.SOLD;
            default -> ProductStatus.AVAILABLE;
        };
    }
}
