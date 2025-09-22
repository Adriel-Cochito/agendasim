#!/bin/bash

echo "Executando testes com cobertura de código..."

# Limpar e compilar o projeto
mvn clean compile

# Executar testes com cobertura
mvn test jacoco:report

# Verificar se os relatórios foram gerados
if [ -f "target/site/jacoco/index.html" ]; then
    echo ""
    echo "========================================"
    echo "Relatório de cobertura gerado com sucesso!"
    echo "========================================"
    echo "Abra o arquivo: target/site/jacoco/index.html"
    echo ""
    echo "Cobertura de instruções:"
    grep "INSTRUCTION" target/site/jacoco/jacoco.csv
    echo ""
else
    echo "Erro: Relatório de cobertura não foi gerado"
    exit 1
fi

# Executar verificação de cobertura
mvn jacoco:check

echo ""
echo "Testes concluídos!"
