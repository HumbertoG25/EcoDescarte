# Estágio 1: Build da aplicação com Maven e Java 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e gera o JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# Estágio 2: Imagem final leve apenas com JRE 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Limita uso de memória para rodar bem no plano gratuito do Render (512 MB)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
