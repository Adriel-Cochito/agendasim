# AgendaSim API

Sistema de agendamento com autenticação JWT.

## 🚀 Deploy no Render (Docker)

### 1. Configurações de Ambiente

A aplicação está configurada para rodar no Render usando Docker com as seguintes configurações:

- **Database**: H2 (arquivo persistente)
- **Porta**: Dinâmica (definida pelo Render via variável `PORT`)
- **CORS**: Configurado para aceitar requisições do frontend `https://agendasim.netlify.app`
- **Container**: Docker com OpenJDK 21

### 2. Deploy Automático (Recomendado)

#### Usando render.yaml
1. Faça push do código para o repositório
2. No Render, conecte o repositório
3. O arquivo `render.yaml` será detectado automaticamente
4. Configure as variáveis de ambiente:
   - `SPRING_PROFILES_ACTIVE`: `prod`
   - `JWT_SECRET`: (gerar uma chave secreta forte)
   - `DB_PASSWORD`: (senha para o banco H2)

### 3. Deploy Manual

#### Configurações no Render:
1. **Language/Runtime**: `Docker`
2. **Dockerfile Path**: `./Dockerfile`
3. **Build Command**: (deixe em branco)
4. **Start Command**: (deixe em branco)
5. **Plan**: `Free`

#### Variáveis de Ambiente:
- `SPRING_PROFILES_ACTIVE` = `prod`
- `JWT_SECRET` = (gere uma chave secreta forte)
- `DB_PASSWORD` = (defina uma senha para o banco)

### 4. Health Check

A aplicação expõe um endpoint de health check em `/actuator/health` para monitoramento.

### 5. Endpoints Principais

- **API Base**: `https://seu-app.onrender.com`
- **Swagger UI**: Desabilitado em produção
- **H2 Console**: Desabilitado em produção
- **Health Check**: `/actuator/health`

### 6. CORS

A API está configurada para aceitar requisições dos seguintes origins:
- `http://localhost:3000` (desenvolvimento)
- `http://localhost:3001` (desenvolvimento)
- `https://agendasim.netlify.app` (produção)

## 🛠️ Desenvolvimento Local

### Opção 1: Maven (Recomendado)
```bash
./mvnw spring-boot:run
```

### Opção 2: Docker
```bash
# Build da imagem
docker build -t agendasim-api .

# Executar container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=sua-chave-secreta \
  -e DB_PASSWORD=sua-senha \
  agendasim-api
```

A aplicação estará disponível em `http://localhost:8080`

### Swagger UI Local
- URL: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`

## 📁 Estrutura do Projeto

- `src/main/java/com/agendasim/`: Código fonte Java
- `src/main/resources/`: Configurações e recursos
- `Dockerfile`: Configuração do container Docker
- `render.yaml`: Configuração para deploy no Render
- `.dockerignore`: Arquivos ignorados no build Docker
- `pom.xml`: Dependências Maven

## 🔧 Configurações Docker

### Dockerfile
- Base: OpenJDK 21
- Maven wrapper para build
- H2 database persistente
- Health checks configurados

### Otimizações
- Cache de dependências Maven
- Logs otimizados para produção
- Configurações de performance do Tomcat
- Swagger desabilitado em produção
