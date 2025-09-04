# AgendaSim API

Sistema de agendamento com autenticação JWT.

## Configuração para Deploy no Render

### 1. Configurações de Ambiente

A aplicação está configurada para rodar no Render com as seguintes configurações:

- **Database**: H2 (arquivo persistente)
- **Porta**: Dinâmica (definida pelo Render via variável `PORT`)
- **CORS**: Configurado para aceitar requisições do frontend `https://agendasim.netlify.app`

### 2. Variáveis de Ambiente no Render

Configure as seguintes variáveis de ambiente no painel do Render:

- `SPRING_PROFILES_ACTIVE`: `prod`
- `JWT_SECRET`: (gerar uma chave secreta forte)
- `DB_PASSWORD`: (senha para o banco H2)

### 3. Deploy

#### Opção 1: Usando render.yaml (Recomendado)
1. Faça push do código para o repositório
2. No Render, conecte o repositório
3. O arquivo `render.yaml` será detectado automaticamente

#### Opção 2: Configuração Manual
1. **Build Command**: `./mvnw clean package -DskipTests`
2. **Start Command**: `java -jar target/agendasim-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`
3. **Environment**: `Java`
4. **Plan**: `Free`

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

## Desenvolvimento Local

Para rodar localmente:

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### Swagger UI Local
- URL: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`

## Estrutura do Projeto

- `src/main/java/com/agendasim/`: Código fonte Java
- `src/main/resources/`: Configurações e recursos
- `render.yaml`: Configuração para deploy no Render
- `pom.xml`: Dependências Maven
