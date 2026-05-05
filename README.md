

## Flujo de Trabajo para el Equipo

Para mantener la integridad del proyecto, seguimos este flujo de colaboración:

1.  **Ramas Personales:** Cada integrante trabaja en su propia rama (ej. `dev-dani`, `dev-paco`, `dev-ale`).
2.  **Pull Requests:** Al terminar una tarea, se solicita un Pull Request hacia la rama **`dev`**.
3.  **CI/CD:** GitHub verificará que el código compile correctamente. Si la prueba es exitosa (check verde), se realiza el merge y **Render** actualizará la página web automáticamente.


## Configuración Local

Si deseas ejecutar el proyecto en tu máquina (ej. Linux Mint):

1.  **Clonar el repo:**
    ```bash
    git clone [https://github.com/Danielarubin/ecommerce-project.git](https://github.com/Danielarubin/ecommerce-project.git)
    ```
2.  **Configurar Base de Datos:**
    Asegúrate de tener PostgreSQL corriendo localmente y configurar las credenciales en `application.properties`.
3.  **Ejecutar:**
    ```bash
    ./mvnw spring-boot:run
    ```

---

## Equipo de Desarrollo

*   **Alessandro (ElGhettoBoy):**  Backend, Docker & DevOps.
*   **Daniela (Dani):** Frontend & Diseño (Figma).
*   **Paco:** Desarrollo Backend & Base de Datos

---



---

# Bazar Online - E-commerce Project

Este es un proyecto académico desarrollado para la carrera de **Sistemas Computacionales** en el **ITIZ**. Se trata de una plataforma de comercio electrónico (Bazar) construida con una arquitectura robusta, escalable y con despliegue automatizado.

## Estado del Proyecto
![Build Status](https://img.shields.io/badge/CI%2FCD-Active-brightgreen)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot%203.2.4-blue)
![Docker](https://img.shields.io/badge/Container-Docker-blue)

El proyecto cuenta con un pipeline de **Integración Continua (CI)** mediante GitHub Actions y **Despliegue Continuo (CD)** en Render.

---

## Tecnologías Utilizadas

*   **Backend:** Java 17 con Spring Boot 3.2.4.
*   **Base de Datos:** PostgreSQL (Gestionada con Spring Data JPA).
*   **Contenerización:** Docker para estandarización de entornos.
*   **Infraestructura:** Render (Hosting del servidor).
*   **Gestión de Dependencias:** Maven.

---

## Estructura del Proyecto
```text
src/main/java/com/school/ecommerce/
├── controller/   # Endpoints de la API REST
├── model/        # Entidades de la base de datos (JPA)
├── repository/   # Interfaces para consultas a la DB
└── service/      # Lógica de negocio
src/main/resources/
└── static/       # Frontend (HTML, CSS, JS de Daniela)
