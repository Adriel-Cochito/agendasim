# Use a imagem oficial do OpenJDK 21
FROM openjdk:21-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Instala dependências necessárias
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copia o wrapper do Maven
COPY mvnw .
COPY .mvn .mvn

# Copia o arquivo pom.xml
COPY pom.xml .

# Torna o mvnw executável
RUN chmod +x ./mvnw

# Baixa as dependências (para cache do Docker)
RUN ./mvnw dependency:go-offline -B

# Copia o código fonte
COPY src src

# Compila a aplicação
RUN ./mvnw clean package -DskipTests

# Cria diretório para dados do H2
RUN mkdir -p data

# Expõe a porta
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "target/agendasim-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
