package com.school.ecommerce.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role != null ? role.name() : Role.BUYER.name();
    }

    @Override
    public Role convertToEntityAttribute(String value) {
        if (value == null || value.isBlank()) {
            return Role.BUYER;
        }

        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "ADMIN", "ROLE_ADMIN", "ADMINISTRADOR" -> Role.ADMIN;
            case "SELLER", "ROLE_SELLER", "VENDEDOR", "CREADOR" -> Role.SELLER;
            case "BUYER", "ROLE_BUYER", "COMPRADOR", "USER", "USUARIO" -> Role.BUYER;
            default -> Role.BUYER;
        };
    }
}
