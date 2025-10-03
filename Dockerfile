# ==============================================================================
# STAGE 1: Build da aplicação
# ==============================================================================
FROM eclipse-temurin:21-jdk as builder

# Define o diretório de trabalho
WORKDIR /app

# Instala dependências necessárias para o build
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copia arquivos do Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Copia o arquivo pom.xml
COPY pom.xml .

# Torna o mvnw executável
RUN chmod +x ./mvnw

# Baixa as dependências (para otimizar cache do Docker)
RUN ./mvnw dependency:go-offline -B

# Copia o código fonte
COPY src src

# Compila a aplicação
RUN ./mvnw clean package -DskipTests -Dmaven.compiler.showDeprecation=false

# ==============================================================================
# STAGE 2: Runtime da aplicação
# ==============================================================================
FROM eclipse-temurin:21-jre

# Instala dependências de runtime
RUN apt-get update && apt-get install -y \
    curl \
    dumb-init \
    && rm -rf /var/lib/apt/lists/*

# Cria usuário não-root para segurança
RUN groupadd -r agendasim && useradd -r -g agendasim agendasim

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR do stage de build
COPY --from=builder /app/target/agendasim-0.0.1-SNAPSHOT.jar app.jar

# Ajusta permissões
RUN chown -R agendasim:agendasim /app
USER agendasim

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expõe a porta
EXPOSE 8080

# Usa dumb-init para gerenciar sinais corretamente
ENTRYPOINT ["dumb-init", "--"]

# Comando para executar a aplicação com configurações otimizadas
CMD ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar", \
    "--spring.profiles.active=prod"]
