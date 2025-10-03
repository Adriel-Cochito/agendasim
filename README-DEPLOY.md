# 🚀 AgendaSim API - Deploy para VPS Hostinger

Este guia explica como preparar e fazer deploy da API AgendaSim em uma VPS da Hostinger usando Docker e PostgreSQL.

## 📋 Pré-requisitos na VPS

1. **Docker e Docker Compose instalados**
2. **Git instalado**
3. **Acesso SSH à VPS**
4. **Portas 8080 e 5432 liberadas no firewall**

## 🛠️ Preparação do Ambiente

### 1. Clone o repositório na VPS

```bash
git clone <seu-repositorio>
cd agendasim-api
```

### 2. Configure as variáveis de ambiente

```bash
# Copie o arquivo de exemplo
cp env.example .env

# Edite o arquivo .env
nano .env
```

**⚠️ IMPORTANTE:** Configure estas variáveis obrigatórias no `.env`:

```env
# Banco de dados
POSTGRES_PASSWORD=sua_senha_super_segura_aqui
POSTGRES_DB=agendasim
POSTGRES_USER=postgres

# JWT (gere uma chave segura)
JWT_SECRET=sua_chave_jwt_super_secreta_e_longa_aqui

# API
API_PORT=8080

# CORS (ajuste para seu frontend no Netlify)
CORS_ALLOWED_ORIGINS=https://seu-app.netlify.app
```

### 3. Gere uma chave JWT segura

```bash
# Gerar chave JWT segura
openssl rand -base64 64
```

## 🚀 Deploy

### Opção 1: Script automatizado (Linux)

```bash
# Torne o script executável
chmod +x deploy.sh

# Execute o deploy
./deploy.sh
```

### Opção 2: Comandos manuais

```bash
# Parar containers existentes
docker-compose down --remove-orphans

# Build e iniciar
docker-compose up --build -d

# Verificar status
docker-compose ps

# Ver logs
docker-compose logs -f agendasim-api
```

## 📊 Monitoramento

### Verificar saúde da aplicação

```bash
# Health check
curl http://localhost:8080/api/actuator/health

# Script de monitoramento completo
chmod +x monitor.sh
./monitor.sh
```

### Logs importantes

```bash
# Logs da API
docker-compose logs -f agendasim-api

# Logs do PostgreSQL
docker-compose logs -f postgres

# Todos os logs
docker-compose logs -f
```

## 🗄️ Backup do Banco de Dados

### Backup automático

```bash
# Tornar script executável
chmod +x backup.sh

# Executar backup
./backup.sh
```

### Backup manual

```bash
# Backup
docker exec agendasim-postgres pg_dump -U postgres -d agendasim > backup.sql

# Restaurar
docker exec -i agendasim-postgres psql -U postgres -d agendasim < backup.sql
```

## 🔧 Configurações de Produção

### Configurações importantes aplicadas:

- ✅ **Multi-stage build** para imagem otimizada
- ✅ **Usuário não-root** para segurança
- ✅ **Health checks** configurados
- ✅ **Volume persistente** para PostgreSQL
- ✅ **Connection pool** otimizado
- ✅ **Logging** configurado
- ✅ **CORS** configurável
- ✅ **Compressão HTTP** habilitada
- ✅ **Headers de segurança** configurados

### Endpoints importantes:

- **API Base:** `http://seu-ip:8080/api`
- **Health Check:** `http://seu-ip:8080/api/actuator/health`
- **Metrics:** `http://seu-ip:8080/api/actuator/metrics`

## 🔒 Segurança

### Configurações aplicadas:

1. **Usuário não-root** no container
2. **JWT com chave forte**
3. **Headers de segurança**
4. **CORS configurado**
5. **Cookies seguros**
6. **Swagger desabilitado** em produção

### Recomendações adicionais:

1. **Configure um proxy reverso** (Nginx)
2. **Use HTTPS** com certificado SSL
3. **Configure firewall** adequadamente
4. **Monitore logs** regularmente
5. **Faça backups** periódicos

## 🔄 Atualizações

```bash
# Atualizar código
git pull origin main

# Rebuild e restart
docker-compose up --build -d

# Verificar se tudo está funcionando
./monitor.sh
```

## 🐛 Solução de Problemas

### Container não inicia

```bash
# Ver logs detalhados
docker-compose logs agendasim-api

# Verificar configurações
docker-compose config
```

### Banco não conecta

```bash
# Verificar se PostgreSQL está rodando
docker-compose ps postgres

# Testar conexão
docker exec agendasim-postgres pg_isready -U postgres

# Ver logs do banco
docker-compose logs postgres
```

### API não responde

```bash
# Verificar se a porta está aberta
netstat -tlnp | grep 8080

# Testar localmente
curl http://localhost:8080/api/actuator/health

# Verificar recursos
docker stats
```

## 📁 Estrutura de Arquivos

```
agendasim-api/
├── docker-compose.yml          # Produção
├── docker-compose.dev.yml      # Desenvolvimento
├── Dockerfile                  # Build otimizado
├── .env.example               # Exemplo de configuração
├── deploy.sh                  # Script de deploy (Linux)
├── deploy.bat                 # Script de deploy (Windows)
├── backup.sh                  # Script de backup
├── monitor.sh                 # Script de monitoramento
├── init-scripts/              # Scripts de inicialização do DB
│   └── 01-init.sql
└── src/                       # Código fonte
```

## 🆘 Suporte

Se encontrar problemas:

1. Verifique os logs: `docker-compose logs -f`
2. Execute o monitor: `./monitor.sh`
3. Verifique as configurações: `docker-compose config`
4. Teste a conectividade de rede
5. Verifique recursos disponíveis: `docker stats`

---

**✅ Após o deploy bem-sucedido, sua API estará disponível em:**
- **Base URL:** `http://seu-ip-vps:8080/api`
- **Health Check:** `http://seu-ip-vps:8080/api/actuator/health`
