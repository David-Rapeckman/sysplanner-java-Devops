# SysPlanner – Agenda inteligente em nuvem (Java + Azure DevOps)

## 1. Visão geral

O **SysPlanner** é uma API REST desenvolvida em **Java 17 / Spring Boot** para gerenciamento de usuários e lembretes de rotina (tarefas, compromissos, atividades do dia a dia).

O objetivo do projeto é servir como base para a disciplina **DevOps Tools & Cloud Computing**, demonstrando:

- Aplicação Java com **CRUD completo**.
- Infraestrutura provisionada via **Azure CLI** (PaaS – Web App + Azure SQL).
- Integração ponta a ponta com **Azure DevOps**:
  - Azure Repos (Git + branching + PR).
  - Azure Boards (Work Items vinculados a commits/PRs).
  - Azure Pipelines (Build + Release com deploy automático).

---

## 2. Arquitetura da solução

### 2.1 Componentes principais

- **Aplicação**:  
  - Java 17  
  - Spring Boot (API REST)  
  - Maven (build)

- **Back-end em nuvem (PaaS)**  
  - **Azure App Service** (Web App): `sysplanner-java-001`  
  - **Azure App Service Plan**: plano Linux básico (B1)  
  - **Azure SQL Database**: banco relacional para persistência dos dados  
  - **Resource Group**: `rg-sysplanner-java`

- **DevOps / ALM (Azure DevOps)**  
  - Organização: `DEVOPS-RM556607`  
  - Projeto: `SysPlanner`  
  - Repositório Git privado com branch `main` protegida  
  - Azure Boards para controle de tarefas / PBIs  
  - Azure Pipelines (Build + Release)

### 2.2 Fluxo de alto nível (Arquitetura Macro)

1. **Desenvolvedor** faz alterações no código local e envia para o **Azure Repos** (branch de feature).
2. É aberta uma **Pull Request** para a branch `main`, vinculada a um **Work Item** do Azure Boards.
3. Após o merge na `main`, a **pipeline de Build** é disparada:
   - Executa `mvn clean package` (com `-DskipTests` na CI).
   - Gera o artefato JAR `sysplanner-0.0.1-SNAPSHOT.jar`.
   - Publica o JAR como artefato `drop`.
4. A **pipeline de Release** é disparada automaticamente:
   - Consome o artefato `drop`.
   - Faz o deploy do JAR no **Web App `sysplanner-java-001`**.
5. A aplicação em produção se conecta ao **Azure SQL** usando connection string configurada no Web App.
6. O **usuário final** acessa a API pela URL pública do App Service e consome os endpoints REST (CRUD).

Um diagrama dessa arquitetura é mantido na pasta `/docs`.

---

## 3. Infraestrutura em Azure (scripts)

Toda a infraestrutura é provisionada e/ou configurada via **scripts** no repositório (conforme rubrica da GS):

- `script-infra-sysplanner.ps1`  
  - Cria/valida:
    - Resource Group `rg-sysplanner-java`
    - App Service Plan (Linux, SKU B1)
    - Web App `sysplanner-java-001`
    - Azure SQL Server + Azure SQL Database
    - Regras de firewall padrão (AllowAzureServices)
- `config-webapp.ps1`  
  - Configura connection string e App Settings para o Spring Boot (datasource URL, usuário, senha etc.).
- `deploy-jar.ps1`  
  - Publica o JAR da aplicação no Web App usando `az webapp deploy`.
- `scripts/script-bd.sql`  
  - Script de criação das tabelas principais e dados iniciais no Azure SQL.

> **Obs.:** o script `script-bd.sql` pode ser executado via **Query Editor** do Azure Portal, apontando para o banco configurado.

---

## 4. Modelo de dados (script-bd.sql)

O banco de dados é simplificado para evidenciar o CRUD em pelo menos duas tabelas. Exemplo de modelagem:

- `USUARIO`
- `LEMBRETE`

### 4.1 Exemplo de DDL resumida

```sql
CREATE TABLE USUARIO (
    ID          INT IDENTITY(1,1) PRIMARY KEY,
    NOME        VARCHAR(100) NOT NULL,
    EMAIL       VARCHAR(150) NOT NULL UNIQUE,
    SENHA       VARCHAR(255) NOT NULL,
    ATIVO       BIT NOT NULL DEFAULT 1,
    DATA_CADASTRO DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE LEMBRETE (
    ID              INT IDENTITY(1,1) PRIMARY KEY,
    TITULO          VARCHAR(150) NOT NULL,
    DESCRICAO       VARCHAR(500) NULL,
    DATA_LEMBRETE   DATETIME2 NOT NULL,
    PRIORIDADE      VARCHAR(20) NOT NULL,
    STATUS          VARCHAR(20) NOT NULL,
    ID_USUARIO      INT NOT NULL,
    CONSTRAINT FK_LEMBRETE_USUARIO FOREIGN KEY (ID_USUARIO)
        REFERENCES USUARIO(ID)
);
```

### 4.2 Inserts básicos (para testes no vídeo)

```sql
INSERT INTO USUARIO (NOME, EMAIL, SENHA)
VALUES ('Usuário Demo', 'demo@sysplanner.com', 'senha123');

INSERT INTO LEMBRETE (TITULO, DESCRICAO, DATA_LEMBRETE, PRIORIDADE, STATUS, ID_USUARIO)
VALUES (
  'Reunião de alinhamento',
  'Reunião com o time para planejar a semana',
  DATEADD(DAY, 1, SYSDATETIME()),
  'ALTA',
  'PENDENTE',
  1
);
```

---

## 5. API REST – Endpoints e exemplos em JSON

A API segue o padrão REST e expõe principalmente recursos de **Usuários** e **Lembretes**.

### 5.1 Usuários – `/api/usuarios`

Operações típicas:

- `GET /api/usuarios` → Lista todos os usuários.
- `GET /api/usuarios/{id}` → Busca usuário por ID.
- `POST /api/usuarios` → Cria novo usuário.
- `PUT /api/usuarios/{id}` → Atualiza usuário existente.
- `DELETE /api/usuarios/{id}` → Remove (ou inativa) usuário.

#### Exemplo – Criar Usuário (POST)

Request:

```json
{
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "senha": "senha123"
}
```

Response (201 – Created):

```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "ativo": true,
  "dataCadastro": "2025-11-23T10:00:00"
}
```

---

### 5.2 Lembretes – `/api/lembretes`

Operações típicas:

- `GET /api/lembretes` → Lista todos os lembretes.
- `GET /api/lembretes/{id}` → Detalha um lembrete específico.
- `GET /api/lembretes/usuario/{idUsuario}` → Lista lembretes de um usuário.
- `POST /api/lembretes` → Cria novo lembrete.
- `PUT /api/lembretes/{id}` → Atualiza lembrete.
- `DELETE /api/lembretes/{id}` → Exclui lembrete.

#### Exemplo – Criar Lembrete (POST)

Request:

```json
{
  "titulo": "Estudar DevOps",
  "descricao": "Revisar scripts de infra e pipelines",
  "dataLembrete": "2025-11-25T19:00:00",
  "prioridade": "ALTA",
  "status": "PENDENTE",
  "idUsuario": 1
}
```

Response (201 – Created):

```json
{
  "id": 10,
  "titulo": "Estudar DevOps",
  "descricao": "Revisar scripts de infra e pipelines",
  "dataLembrete": "2025-11-25T19:00:00",
  "prioridade": "ALTA",
  "status": "PENDENTE",
  "usuario": {
    "id": 1,
    "nome": "João Silva"
  }
}
```

---

## 6. Execução local (desenvolvimento)

### 6.1 Pré‑requisitos

- **Java 17**
- **Maven 3.x**
- Banco de dados:
  - Pode usar o próprio **Azure SQL** ou um SQL local equivalente (ajustando a connection string).

### 6.2 Configuração de ambiente

Configurar as variáveis (por exemplo em `application.properties` ou `application.yml`):

```properties
spring.datasource.url=jdbc:sqlserver://<servidor>.database.windows.net:1433;databaseName=SysPlannerDB;encrypt=true;trustServerCertificate=false;loginTimeout=30;
spring.datasource.username=<usuario_sql>
spring.datasource.password=<senha_sql>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 6.3 Rodando a aplicação

```bash
mvn clean package
java -jar target/sysplanner-0.0.1-SNAPSHOT.jar
```

Por padrão a aplicação sobe em `http://localhost:8080`.

---

## 7. CI/CD com Azure DevOps

### 7.1 Build Pipeline (CI)

- Pipeline do tipo **YAML** ou classic (conforme configurado no projeto).
- Ações principais:
  - Checkout do repositório `SysPlanner`.
  - Seleção do Java 17.
  - Execução de `mvn clean package` (com `-DskipTests` na pipeline).
  - Publicação do artefato `drop` contendo o `sysplanner-0.0.1-SNAPSHOT.jar`.

Condições:

- Trigger configurado para branch `main`.
- Build é acionado **após merge via Pull Request** (branch policies da `main`).

### 7.2 Release Pipeline (CD)

- Release clássico com estágio **SysPlanner**:
  - Artefato de origem: pipeline de build `SysPlanner`, artefato `drop`.
  - Continuous deployment habilitado.
  - Tarefa **Deploy Azure App Service**:
    - Subscription: **Azure for Students**.
    - App Service name: `sysplanner-java-001`.
    - Package: JAR gerado pelo build.

Resultado:

- A cada merge na `main`, um novo build gera o artefato e o release atualiza a aplicação em produção automaticamente.

---

## 8. Branching, Policies e Boards

### 8.1 Estratégia de Branching

- `main`: branch protegida, sempre estável.
- `feature/...`: branches de desenvolvimento (ex.: `feature/crud-lembretes`, `feature/ajuste-layout-home`).

### 8.2 Branch policies na `main`

Configuradas no Azure Repos:

- ✅ **Minimum number of reviewers**: 1  
- ✅ **Check for linked Work Items**: obrigatório  
- ✅ **Revisor padrão**: RM do aluno  
- (Opcional) **Build validation**: usar a pipeline de build como validação da PR.

### 8.3 Integração Boards ↔ Repos

- Cada tarefa/PBI no Boards gera uma branch de feature.
- Commits referenciam o Work Item (ex.: `#1`).
- A PR é vinculada ao Work Item.
- No vídeo da GS é mostrado o fluxo completo:
  - criar tarefa → branch → commit → PR → build → release → tarefa concluída.

---

## 9. Pastas importantes do repositório

- `/src` – código-fonte Java (controllers, services, entities, repositories etc.).
- `/docs` – diagramas e documentação complementar (ex.: arquitetura macro).
- `/scripts`
  - `script-infra-sysplanner.ps1`
  - `config-webapp.ps1`
  - `deploy-jar.ps1`
  - `script-bd.sql`
- `azure-pipelines.yml` (se usado YAML para o build).
- `Dockerfile` (reservado para experimentos futuros com container; **não utilizado** nesta GS).
- `README.md` (este arquivo).

---

## 10. Checklist para a GS

- [x] Código fonte em repositório privado no Azure Repos  
- [x] Infraestrutura em Azure criada via **scripts CLI**  
- [x] Banco de dados PaaS (Azure SQL) com `script-bd.sql` em `/scripts`  
- [x] Pipelines de **Build** e **Release** configuradas  
- [x] Deploy automático no **Web App `sysplanner-java-001`**  
- [x] Branch `main` protegida + políticas + integração Boards ↔ Repos  
- [x] CRUD documentado em JSON neste README  
- [x] Diagrama de arquitetura na pasta `/docs`  
- [x] Vídeo explicativo mostrando o fluxo completo (Boards → Repos → Pipelines → Azure → CRUD)  

SysPlanner não é só um CRUD em nuvem – ele é o laboratório prático para consolidar todo o fluxo de **DevOps na Azure**, do commit até a aplicação rodando em produção.
