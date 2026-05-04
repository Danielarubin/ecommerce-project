
# Usamos una imagen de Maven con Java 17 para compilar el código
FROM maven:3.8.5-openjdk-17-slim AS build

# Copiamos el código fuente y el pom.xml al contenedor
COPY . .

 
# el despliegue sea más rápido en el servidor gratuito.
RUN mvn clean package -DskipTests


# Usamos una imagen de Java más ligera para correr la app
FROM openjdk:17-jdk-slim

COPY --from=build /target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 que es el que usa Spring Boot por defecto
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]