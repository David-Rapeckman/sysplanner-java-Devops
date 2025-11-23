# ================================
# Build Stage
# ================================
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia apenas os arquivos do Maven para cache de dependências
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Dá permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# Baixa dependências
RUN ./mvnw dependency:go-offline -B

# Copia o restante do código
COPY src ./src

# Build do projeto (ignora testes)
RUN ./mvnw clean package -DskipTests

# ================================
# Runtime Stage
# ================================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o JAR final do build
COPY --from=build /app/target/sysplanner-*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
