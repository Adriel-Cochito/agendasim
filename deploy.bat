@echo off
REM ============================================================================
REM Script de Deploy para VPS Hostinger (Windows)
REM ============================================================================

echo 🚀 Iniciando deploy do AgendaSim...

REM Verificar se o arquivo .env existe
if not exist .env (
    echo ERROR: Arquivo .env nao encontrado! Copie o env.example para .env e configure as variaveis.
    pause
    exit /b 1
)

echo Carregando variaveis de ambiente...
for /f "tokens=1,2 delims==" %%a in (.env) do set %%a=%%b

REM Verificar variáveis obrigatórias
if "%POSTGRES_PASSWORD%"=="" (
    echo ERROR: Variavel POSTGRES_PASSWORD e obrigatoria no arquivo .env
    pause
    exit /b 1
)

if "%JWT_SECRET%"=="" (
    echo ERROR: Variavel JWT_SECRET e obrigatoria no arquivo .env
    pause
    exit /b 1
)

REM Criar diretórios necessários se não existirem
echo Criando diretorios necessarios...
if not exist logs mkdir logs
if not exist backups mkdir backups

REM Parar containers existentes (se houver)
echo Parando containers existentes...
docker-compose down --remove-orphans

REM Build e start dos containers
echo Construindo e iniciando containers...
docker-compose up --build -d

REM Aguardar um pouco
echo Aguardando containers iniciarem...
timeout /t 30 /nobreak > nul

REM Verificar logs
echo Ultimos logs da aplicacao:
docker-compose logs --tail=20 agendasim-api

REM Mostrar status final
echo Status dos containers:
docker-compose ps

echo.
echo 🎉 Deploy concluido!
echo 📊 Acesse o health check em: http://localhost:%API_PORT%/api/actuator/health
echo 📋 Para ver logs: docker-compose logs -f agendasim-api
echo 🛑 Para parar: docker-compose down
echo.
pause
