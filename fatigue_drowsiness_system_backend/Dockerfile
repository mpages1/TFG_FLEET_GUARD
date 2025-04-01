# Etapa 1: Compilar l'aplicació amb Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Executar l'aplicació amb espera a la base de dades
FROM openjdk:17-jdk-alpine
RUN apk add --no-cache bash
WORKDIR /app

# Copiar el .jar generat
COPY --from=build /app/target/*.jar app.jar

# Copiar l'script wait-for-it
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

# Exposar el port del backend
EXPOSE 8080

# Executar el wait-for-it abans d'arrencar l'app
ENTRYPOINT ["./wait-for-it.sh", "postgres", "5432", "--"]
CMD ["java", "-jar", "app.jar"]
