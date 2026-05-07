package com.school.ecommerce.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        String errorCode = "Error";
        String errorTitle = "Algo salió mal";
        String errorMessage = "Hemos detectado un problema técnico. Nuestro equipo ya fue notificado.";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorCode = "404";
                errorTitle = "Página No Encontrada";
                errorMessage = "La página que buscas no existe, ha sido movida o te perdiste navegando.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorCode = "403";
                errorTitle = "Acceso Denegado";
                errorMessage = "No tienes permiso para ver esta página. Por favor, inicia sesión.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorCode = "500";
                errorTitle = "Error Interno del Servidor";
                errorMessage = "Nuestros servidores están experimentando dificultades. Vuelve a intentarlo en unos minutos.";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                errorCode = "401";
                errorTitle = "No Autorizado";
                errorMessage = "Debes iniciar sesión para acceder a este recurso.";
            } else {
                errorCode = String.valueOf(statusCode);
            }
        }

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }
}
