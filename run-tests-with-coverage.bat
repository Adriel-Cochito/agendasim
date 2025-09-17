@echo off
echo Executando testes com cobertura de código...

REM Limpar e compilar o projeto
mvn clean compile

REM Executar testes com cobertura
mvn test jacoco:report

REM Verificar se os relatórios foram gerados
if exist "target\site\jacoco\index.html" (
    echo.
    echo ========================================
    echo Relatório de cobertura gerado com sucesso!
    echo ========================================
    echo Abra o arquivo: target\site\jacoco\index.html
    echo.
    echo Cobertura de instruções:
    type target\site\jacoco\jacoco.csv | findstr "INSTRUCTION"
    echo.
) else (
    echo Erro: Relatório de cobertura não foi gerado
    exit /b 1
)

REM Executar verificação de cobertura
mvn jacoco:check

echo.
echo Testes concluídos!
pause
