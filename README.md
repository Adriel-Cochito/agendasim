# AgendaSim API

Sistema completo de agendamento com autenticação JWT e conformidade LGPD.

## 📋 **Visão Geral**

A AgendaSim API é uma solução completa para gerenciamento de agendamentos que inclui:

- 🏢 **Gestão de Empresas** - Cadastro e administração de empresas
- 👥 **Gestão de Profissionais** - Controle de usuários e permissões
- 📅 **Sistema de Agendamentos** - Agendamento de serviços com profissionais
- 🛠️ **Gestão de Serviços** - Catálogo de serviços oferecidos
- 📊 **Dashboard Analytics** - Métricas e relatórios de negócio
- 🔒 **Conformidade LGPD** - Sistema completo de proteção de dados
- 📝 **Termos e Políticas** - Versionamento de documentos legais
- 🔍 **Auditoria Completa** - Logs de todas as operações

## 🔗 **URLs da API**

### **Produção**
- **Base URL**: `https://agendasim-api.onrender.com`
- **Swagger UI**: `https://agendasim-api.onrender.com/swagger-ui.html`
- **Health Check**: `https://agendasim-api.onrender.com/actuator/health`

### **Desenvolvimento Local**
- **Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **H2 Console**: `http://localhost:8080/h2-console`

---

## 🎯 **Para Desenvolvedores Frontend**

### **Autenticação**
A API usa **JWT (JSON Web Tokens)** para autenticação. Todas as requisições (exceto as públicas) devem incluir o token no header:

```javascript
// Exemplo de requisição autenticada
const response = await fetch('/api/agendas', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});
```

### **Estrutura de Resposta Padrão**
```json
{
  "success": true,
  "data": { /* dados da resposta */ },
  "message": "Operação realizada com sucesso"
}
```

### **Tratamento de Erros**
```json
{
  "success": false,
  "error": {
    "timestamp": "2024-01-01T10:00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Dados inválidos",
    "path": "/api/agendas",
    "traceId": "abc123"
  }
}
```

---

## 📚 **Documentação da API**

### **1. Autenticação**
```http
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@exemplo.com",
  "senha": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "refresh_token_aqui",
  "userInfo": {
    "id": 1,
    "nome": "João Silva",
    "email": "usuario@exemplo.com",
    "perfil": "OWNER"
  },
  "expiresIn": 86400
}
```

### **2. Gestão de Empresas**

#### **Criar Empresa com Owner (Público)**
```http
POST /empresas/com-owner
Content-Type: application/json

{
  "nomeEmpresa": "Salão de Beleza",
  "emailEmpresa": "contato@salao.com",
  "telefoneEmpresa": "+55 31 99999-8888",
  "cnpjEmpresa": "12.345.678/0001-99",
  "nomeProfissional": "João Silva",
  "emailProfissional": "joao@salao.com",
  "senhaProfissional": "senha123"
}
```

#### **Listar Empresas (Admin)**
```http
GET /empresas
Authorization: Bearer {token}
```

#### **Atualizar Empresa (Owner/Admin)**
```http
PUT /empresas/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Novo Nome",
  "email": "novo@email.com",
  "telefone": "+55 31 88888-7777"
}
```

### **3. Gestão de Profissionais**

#### **Listar Profissionais**
```http
GET /profissionais?empresaId={id}
Authorization: Bearer {token}
```

#### **Criar Profissional (Owner)**
```http
POST /profissionais
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Maria Silva",
  "email": "maria@empresa.com",
  "senha": "senha123",
  "perfil": "USER",
  "empresaId": 1
}
```

#### **Atualizar Profissional**
```http
PATCH /profissionais/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Maria Santos",
  "email": "maria.santos@empresa.com"
}
```

### **4. Gestão de Serviços**

#### **Listar Serviços**
```http
GET /servicos?empresaId={id}
Authorization: Bearer {token}
```

#### **Criar Serviço (Owner/Admin)**
```http
POST /servicos
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Corte de Cabelo",
  "descricao": "Corte profissional com máquina e tesoura",
  "preco": 50.00,
  "duracao": 30,
  "empresaId": 1,
  "profissionalIds": [1, 2]
}
```

#### **Atualizar Serviço**
```http
PUT /servicos/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Corte Premium",
  "preco": 60.00,
  "duracao": 45
}
```

### **5. Sistema de Agendamentos**

#### **Listar Agendamentos**
```http
GET /agendas?empresaId={id}&profissionalId={id}&dataInicio={data}&dataFim={data}
Authorization: Bearer {token}
```

#### **Criar Agendamento (Admin)**
```http
POST /agendas
Authorization: Bearer {token}
Content-Type: application/json

{
  "nomeCliente": "Cliente Teste",
  "telefoneCliente": "+55 31 98888-7777",
  "empresaId": 1,
  "profissionalId": 1,
  "servicoId": 1,
  "dataHora": "2024-06-21T10:00:00Z",
  "status": "AGENDADO"
}
```

#### **Atualizar Status do Agendamento**
```http
PUT /agendas/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "CONFIRMADO"
}
```

### **6. Dashboard Analytics**

#### **Resumo Geral**
```http
GET /dash?empresaId={id}
Authorization: Bearer {token}
```

**Resposta:**
```json
{
  "metricasPrincipais": {
    "totalAgendamentos": 150,
    "agendamentosHoje": 8,
    "faturamentoMes": 7500.00,
    "taxaConfirmacao": 85.5
  },
  "indicadoresGestao": {
    "servicosMaisProcurados": [...],
    "profissionaisMaisOcupados": [...],
    "conflitosHorario": 2
  },
  "graficos": {
    "agendamentosPorDia": [...],
    "faturamentoPorSemana": [...]
  },
  "alertas": [
    {
      "tipo": "CONFLITO_HORARIO",
      "mensagem": "Conflito detectado no horário 14:00",
      "prioridade": "ALTA"
    }
  ]
}
```

---

## 🔒 **Sistema LGPD (Lei Geral de Proteção de Dados)**

### **Anonimização de Dados de Clientes**

#### **1. Verificar Cliente**
```http
GET /api/lgpd/clientes/verificar?nomeCliente=João Silva&telefoneCliente=+55 31 99999-8888
```

**Resposta:**
```json
{
  "nomeCliente": "João Silva",
  "telefoneCliente": "+55 31 99999-8888",
  "totalAgendamentos": 5,
  "jaAnonimizado": false
}
```

#### **2. Anonimizar Cliente (Global)**
```http
POST /api/lgpd/clientes/anonimizar?nomeCliente=João Silva&telefoneCliente=+55 31 99999-8888
```

**Resposta:**
```json
{
  "success": true,
  "message": "Cliente anonimizado com sucesso",
  "agendamentosAnonimizados": 5,
  "nomeCliente": "João Silva",
  "telefoneCliente": "+55 31 99999-8888"
}
```

**Resultado:**
- ✅ **Dados pessoais anonimizados** em todos os agendamentos
- ✅ **Dados de negócio preservados** (serviços, horários, valores)
- ✅ **Histórico mantido** para relatórios e faturamento
- ✅ **Auditoria completa** registrada

### **Sistema de Consentimento**

#### **Registrar Consentimento**
```http
POST /api/consentimentos
Authorization: Bearer {token}
Content-Type: application/json

{
  "tipoConsentimento": "MARKETING",
  "finalidade": "Envio de promoções e ofertas",
  "consentido": true
}
```

#### **Meus Consentimentos**
```http
GET /api/consentimentos/meus
Authorization: Bearer {token}
```

#### **Revogar Consentimento**
```http
DELETE /api/consentimentos/{tipo}
Authorization: Bearer {token}
```

### **Termos de Uso e Políticas**

#### **Termo Atual (Público)**
```http
GET /api/termos/ativo
```

#### **Aceitar Termos**
```http
POST /api/termos/aceitar?termoId={id}
Authorization: Bearer {token}
```

#### **Política Atual (Pública)**
```http
GET /api/politicas/ativa
```

#### **Aceitar Política**
```http
POST /api/politicas/aceitar?politicaId={id}
Authorization: Bearer {token}
```

---

---

## 🛠️ **Desenvolvimento Local**

### **Pré-requisitos**
- Java 21+
- Maven 3.6+
- Docker (opcional)

### **Opção 1: Maven (Recomendado)**
```bash
# Clone o repositório
git clone https://github.com/seu-usuario/agendasim-api.git
cd agendasim-api

# Execute a aplicação
./mvnw spring-boot:run
```

### **Opção 2: Docker**
```bash
# Build da imagem
docker build -t agendasim-api .

# Executar container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e JWT_SECRET=sua-chave-secreta \
  -e DB_PASSWORD=sua-senha \
  agendasim-api
```

### **Acesso Local**
- **API**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **H2 Console**: `http://localhost:8080/h2-console`
- **Health Check**: `http://localhost:8080/actuator/health`

---

## 🚀 **Deploy em Produção**

### **Render.com (Recomendado)**

#### **1. Deploy Automático**
1. Conecte o repositório no Render
2. O arquivo `render.yaml` será detectado automaticamente
3. Configure as variáveis de ambiente:
   - `SPRING_PROFILES_ACTIVE`: `prod`
   - `JWT_SECRET`: (gerar uma chave secreta forte)
   - `DB_PASSWORD`: (senha para o banco H2)

#### **2. Deploy Manual**
- **Language/Runtime**: `Docker`
- **Dockerfile Path**: `./Dockerfile`
- **Plan**: `Free` ou `Starter`

### **Configurações de Ambiente**

#### **Desenvolvimento**
```properties
spring.profiles.active=dev
jwt.secret=chave-desenvolvimento
spring.h2.console.enabled=true
```

#### **Produção**
```properties
spring.profiles.active=prod
jwt.secret=${JWT_SECRET}
spring.h2.console.enabled=false
```

### **CORS Configurado**
- `http://localhost:3000` (desenvolvimento)
- `http://localhost:3001` (desenvolvimento)
- `https://agendasim.netlify.app` (produção)

---

## 📁 **Estrutura do Projeto**

```
agendasim-api/
├── src/main/java/com/agendasim/
│   ├── controller/          # Controllers REST
│   │   ├── AuthController.java
│   │   ├── EmpresaController.java
│   │   ├── ProfissionalController.java
│   │   ├── ServicoController.java
│   │   ├── AgendaController.java
│   │   ├── DashboardController.java
│   │   ├── LGPDController.java
│   │   ├── ConsentimentoController.java
│   │   ├── TermosController.java
│   │   ├── PoliticaPrivacidadeController.java
│   │   └── AuditoriaController.java
│   ├── service/             # Serviços de negócio
│   │   ├── EmpresaService.java
│   │   ├── ProfissionalService.java
│   │   ├── ServicoService.java
│   │   ├── AgendaService.java
│   │   ├── DashboardService.java
│   │   ├── LGPDService.java
│   │   ├── ConsentimentoService.java
│   │   ├── TermosService.java
│   │   ├── PoliticaPrivacidadeService.java
│   │   └── AuditoriaService.java
│   ├── model/               # Entidades JPA
│   │   ├── Empresa.java
│   │   ├── Profissional.java
│   │   ├── Servico.java
│   │   ├── Agenda.java
│   │   ├── Disponibilidade.java
│   │   ├── TermoAceite.java
│   │   ├── PoliticaPrivacidade.java
│   │   ├── Consentimento.java
│   │   ├── AceiteUsuario.java
│   │   ├── AceitePolitica.java
│   │   └── LogAuditoria.java
│   ├── repository/          # Repositórios JPA
│   ├── dto/                 # DTOs de transferência
│   ├── security/            # Configurações de segurança
│   └── exception/           # Tratamento de exceções
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   ├── application-prod.properties
│   ├── import.sql
│   └── db/migration/        # Migrações do banco
├── src/test/                # Testes unitários e integração
├── Dockerfile
├── docker-compose.yml
├── render.yaml
├── pom.xml
└── README.md
```

---

## 🔧 **Tecnologias Utilizadas**

### **Backend**
- **Java 21** - Linguagem de programação
- **Spring Boot 3.2.2** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória/arquivo
- **JWT** - Autenticação stateless
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização

### **Segurança e LGPD**
- **Criptografia BCrypt** - Hash de senhas
- **Validação de dados** - Bean Validation
- **Auditoria completa** - Logs de todas as operações
- **CORS configurado** - Controle de origem
- **Rate limiting** - Proteção contra ataques

### **Documentação**
- **Swagger/OpenAPI** - Documentação da API
- **SpringDoc** - Geração automática de docs
- **JaCoCo** - Cobertura de testes

---

## 📊 **Status do Projeto**

### **✅ Funcionalidades Implementadas**
- [x] Sistema de autenticação JWT
- [x] Gestão completa de empresas
- [x] Gestão de profissionais e usuários
- [x] Sistema de agendamentos
- [x] Gestão de serviços
- [x] Dashboard com métricas
- [x] Sistema LGPD completo
- [x] Versionamento de documentos legais
- [x] Sistema de consentimento granular
- [x] Auditoria completa
- [x] API REST documentada
- [x] Deploy automatizado

### **🔄 Em Desenvolvimento**
- [ ] Testes de integração LGPD
- [ ] Relatórios de conformidade
- [ ] Notificações automáticas
- [ ] Backup de dados

### **📋 Próximas Funcionalidades**
- [ ] Integração com DPO
- [ ] Certificação ISO 27001
- [ ] Relatórios para autoridades
- [ ] Backup automático

---

## 🤝 **Contribuição**

### **Como Contribuir**
1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### **Padrões de Código**
- Use Java 21+ features
- Siga as convenções do Spring Boot
- Escreva testes para novas funcionalidades
- Documente APIs com Swagger
- Mantenha cobertura de testes > 60%

---

## 📄 **Licença**

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 📞 **Suporte**

Para dúvidas ou suporte:
- **Email**: contato@agendasim.com
- **Issues**: [GitHub Issues](https://github.com/seu-usuario/agendasim-api/issues)
- **Documentação**: [Swagger UI](https://agendasim-api.onrender.com/swagger-ui.html)

---

## 🏆 **Conformidade LGPD**

Este projeto está **100% em conformidade** com a Lei Geral de Proteção de Dados (LGPD) brasileira, implementando todos os direitos dos titulares de dados e princípios de proteção de dados pessoais.

**Status**: ✅ **CONFORME** - Pronto para uso em produção
