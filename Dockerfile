
FROM maven:3.8.5-openjdk-17 AS build
COPY . . 
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

COPY --from=build /target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 que es el que usa Spring Boot por defecto
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]