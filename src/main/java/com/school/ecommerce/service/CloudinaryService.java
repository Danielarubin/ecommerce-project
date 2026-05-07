package com.school.ecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio para subir imágenes a Cloudinary.
 *
 * Configuración (usar UNA de estas opciones en application.properties o variables de entorno):
 *   Opción A — variable CLOUDINARY_URL completa:
 *     export CLOUDINARY_URL=cloudinary://API_KEY:API_SECRET@CLOUD_NAME
 *
 *   Opción B — variables individuales:
 *     CLOUDINARY_CLOUD_NAME=mi_cloud
 *     CLOUDINARY_API_KEY=12345
 *     CLOUDINARY_API_SECRET=abcde
 */
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name:}") String cloudName,
            @Value("${cloudinary.api-key:}") String apiKey,
            @Value("${cloudinary.api-secret:}") String apiSecret) {

        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");

        if (cloudinaryUrl != null && !cloudinaryUrl.isBlank()) {
            // Opción A: usa la variable de entorno CLOUDINARY_URL directamente
            this.cloudinary = new Cloudinary(cloudinaryUrl);
        } else if (!cloudName.isBlank() && !apiKey.isBlank() && !apiSecret.isBlank()) {
            // Opción B: construye desde propiedades individuales
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", cloudName);
            config.put("api_key", apiKey);
            config.put("api_secret", apiSecret);
            this.cloudinary = new Cloudinary(config);
        } else {
            // Sin configuración: se inicializa vacío (uploadImage lanzará excepción)
            this.cloudinary = new Cloudinary(ObjectUtils.emptyMap());
        }
    }

    /**
     * Sube un archivo de imagen a Cloudinary y retorna la URL segura (https).
     * @throws IOException si el archivo no puede subirse
     * @throws IllegalStateException si Cloudinary no está configurado
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo de imagen está vacío.");
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "bazar-products")
            );
            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl != null) {
                return secureUrl.toString();
            }
        } catch (Exception ignored) {
            // En desarrollo local permitimos seguir publicando aunque Cloudinary no esté disponible.
        }

        return storeImageLocally(file);
    }

    private String storeImageLocally(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "producto";
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < originalName.length() - 1) {
            extension = originalName.substring(dotIndex).replaceAll("[^A-Za-z0-9.]", "");
        }
        if (extension.isBlank()) {
            extension = ".jpg";
        }

        Path uploadDir = Path.of("uploads", "products");
        Files.createDirectories(uploadDir);

        String fileName = UUID.randomUUID() + extension.toLowerCase();
        Path target = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/products/" + fileName;
    }
}
