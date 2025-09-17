# Guia de Testes - AgendaSim

Este documento descreve a estrutura de testes implementada no projeto AgendaSim.

## 📋 Estrutura de Testes

### Testes Unitários
- **AgendaServiceTest**: Testa a lógica de negócio do serviço de agendamentos
- **ProfissionalServiceTest**: Testa operações CRUD e validações de profissionais
- **JwtTokenServiceTest**: Testa geração, validação e extração de tokens JWT
- **AgendaControllerTest**: Testa endpoints da API de agendamentos

### Testes de Integração
- **AgendaIntegrationTest**: Testa integração com banco de dados usando @DataJpaTest
- **AgendaServiceIntegrationTest**: Teste completo com TestContainers e PostgreSQL

## 🛠️ Tecnologias Utilizadas

- **JUnit 5**: Framework de testes principal
- **Mockito**: Para mocks e stubs
- **Spring Boot Test**: Anotações e utilitários do Spring
- **TestContainers**: Para testes de integração com banco real
- **JaCoCo**: Para análise de cobertura de código
- **H2 Database**: Banco em memória para testes rápidos
- **PostgreSQL**: Banco real via TestContainers

## 🚀 Como Executar os Testes

### Executar todos os testes
```bash
mvn test
```

### Executar testes com cobertura
```bash
# Windows
run-tests-with-coverage.bat

# Linux/Mac
chmod +x run-tests-with-coverage.sh
./run-tests-with-coverage.sh
```

### Executar apenas testes unitários
```bash
mvn test -Dtest="*Test"
```

### Executar apenas testes de integração
```bash
mvn test -Dtest="*IntegrationTest"
```

## 📊 Cobertura de Código

### Configuração do JaCoCo
- **Meta de cobertura**: 60% de instruções
- **Relatórios gerados**: HTML, XML e CSV
- **Localização**: `target/site/jacoco/`

### Visualizar relatório
Abra o arquivo `target/site/jacoco/index.html` no navegador após executar os testes.

## 🧪 Tipos de Teste Implementados

### 1. Testes de Serviços
- ✅ Criação de registros
- ✅ Busca por ID
- ✅ Listagem com filtros
- ✅ Atualização de dados
- ✅ Exclusão de registros
- ✅ Validação de regras de negócio
- ✅ Tratamento de exceções

### 2. Testes de Controllers
- ✅ Endpoints GET, POST, PUT, DELETE
- ✅ Validação de parâmetros
- ✅ Códigos de resposta HTTP
- ✅ Serialização/Deserialização JSON

### 3. Testes de Segurança
- ✅ Geração de tokens JWT
- ✅ Validação de tokens
- ✅ Extração de claims
- ✅ Tratamento de tokens expirados/inválidos

### 4. Testes de Integração
- ✅ Persistência no banco de dados
- ✅ Transações
- ✅ Relacionamentos entre entidades
- ✅ Consultas complexas

## 📁 Estrutura de Arquivos

```
src/test/java/com/agendasim/
├── service/
│   ├── AgendaServiceTest.java
│   └── ProfissionalServiceTest.java
├── security/
│   └── JwtTokenServiceTest.java
├── controller/
│   └── AgendaControllerTest.java
├── integration/
│   ├── AgendaIntegrationTest.java
│   └── AgendaServiceIntegrationTest.java
└── config/
    └── TestContainersConfig.java

src/test/resources/
├── application-test.properties
└── jacoco-test.properties
```

## 🔧 Configurações

### application-test.properties
- Banco H2 em memória para testes rápidos
- Logs detalhados para debug
- DDL automático (create-drop)

### TestContainersConfig
- PostgreSQL 15 para testes de integração
- Configuração dinâmica de propriedades
- Isolamento completo entre testes

## 📈 Métricas de Cobertura

### Classes com Maior Cobertura
1. **AgendaService**: ~95% (classe mais crítica)
2. **ProfissionalService**: ~90%
3. **JwtTokenService**: ~85%
4. **AgendaController**: ~80%

### Cenários Testados
- ✅ Casos de sucesso
- ✅ Casos de erro
- ✅ Validações de entrada
- ✅ Regras de negócio
- ✅ Conflitos de agendamento
- ✅ Tokens JWT válidos/inválidos

## 🐛 Debugging

### Logs de Teste
Os logs estão configurados para mostrar:
- SQL executado
- Parâmetros de consulta
- Operações de segurança
- Erros detalhados

### H2 Console
Acesse `http://localhost:8080/h2-console` durante os testes para inspecionar o banco.

## 🚨 Troubleshooting

### Problemas Comuns

1. **TestContainers não inicia**
   - Verifique se o Docker está rodando
   - Confirme a conectividade de rede

2. **Cobertura baixa**
   - Execute `mvn clean test jacoco:report`
   - Verifique se todos os testes passaram

3. **Testes falhando**
   - Verifique os logs detalhados
   - Confirme as configurações do banco

## 📝 Próximos Passos

- [ ] Adicionar testes para mais controllers
- [ ] Implementar testes de performance
- [ ] Adicionar testes de carga
- [ ] Configurar CI/CD com cobertura
- [ ] Implementar testes de contrato (Pact)

## 🤝 Contribuindo

Ao adicionar novos testes:
1. Siga o padrão de nomenclatura existente
2. Mantenha cobertura acima de 80%
3. Documente casos complexos
4. Execute todos os testes antes do commit
