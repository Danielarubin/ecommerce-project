package com.school.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para las páginas informativas estáticas:
 *   /faq           — Preguntas frecuentes
 *   /envios        — Política de envíos
 *   /devoluciones  — Política de devoluciones
 *   /contacto      — Formulario de contacto
 */
@Controller
public class InfoController {

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/envios")
    public String envios() {
        return "envios";
    }

    @GetMapping("/devoluciones")
    public String devoluciones() {
        return "devoluciones";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }
}
