# 🚀 SysPlanner

**SysPlanner** é uma aplicação web desenvolvida em **Java + SpringBoot** para gerenciar lembretes criados pelos nossos usuários, oferecendo filtros personalizados, paginação e ordenação. O SysPlanner tem como objetivo utilizar a tecnologia para auxiliar os nossos usuários a se organizarem, no ambiente de trabalho, pessoal, acadêmico, etc.

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [Funcionalidades](#-funcionalidades)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar Localmente](#-como-executar-localmente)
- [Modelo de Dados](#️-modelo-de-dados)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Equipe](#-equipe)

---

## 🧾 Sobre o Projeto

O objetivo do SysPlanner é fornecer uma **Aplicação Web** para cadastro, listagem, e manutenção de lembretes de usuários, com:

- Validações e regras de negócio bem definidas.
- Design amigável
- Mensageria

A aplicação segue boas práticas, utilizando **DTOs**, **Controllers**, **Services** e **Swagger** para documentação interativa.
---
### Destaques

- ✨ Interface de administração intuitiva
- 🔐 Sistema de autenticação e autorização com Spring Security
- 🔍 Filtros dinâmicos e buscas avançadas
- 📱 API RESTful documentada com Swagger
- 🗃️ Persistência de dados com H2 Database

---

## ⚙️ Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - Camada de persistência
- **Spring Security** - Autenticação e autorização
- **Hibernate** - ORM (Object-Relational Mapping)
- **RabbitMQ** (CloudAMQP) - Mensageria
- **Docker** - Deploy no Render
- **Render** (Deployment)

### Banco de Dados
- **Oracle Database**
  
### Ferramentas e Bibliotecas
- **Lombok** - Redução de código boilerplate
- **Jakarta Validation** - Validação de dados
- **Maven** - Gerenciamento de dependências
- **Thymeleaf** - Template engine para páginas web

---

## ✅ Funcionalidades

### Gerenciamento de Pátios
- ✔️ Cadastro, edição e exclusão de pátios
- ✔️ Controle de capacidade máxima


### Gerenciamento de Usuários
- ✔️ Sistema de autenticação
- ✔️ Controle de acesso por perfis
- ✔️ Cadastro e gerenciamento de usuários

### Recursos Técnicos
- 🔎 Filtros dinâmicos com `JpaSpecificationExecutor`
- 📄 Paginação e ordenação em todos os endpoints
- 📖 Validações detalhadas com mensagens amigáveis
- 🧱 Arquitetura em camadas (Controller, Service, Repository)

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- ☕ [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- 📦 [Maven 3.8+](https://maven.apache.org/download.cgi)
- 💻 IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

---

## 🚀 Como Executar Localmente

### 1. Clone o repositório

```bash
git clone https://github.com/guurangel/SysPlannerJava.git
cd SysPlannerJava

```

### 2. Configure as credenciais dentro de application.properties (credenciais estão no .txt)

### 2. Compile o projeto

```bash
mvn clean install
```

### 3. Execute a aplicação

```bash
mvn spring-boot:run
```

### 4. Acesse a aplicação

A aplicação estará disponível em:

- **Aplicação Web:** http://localhost:8080/login
---

## 🗃️ Modelo de Dados

### Usuario

```java
{
  "id": Long,
  "nome": String,
  "email": String,
  "senha": String,
  "cpf": String,
  "Endereco": Endereco,
  "Role": Role
  "Lembrete": List<Lembrete>
}
```

### Endereco

```java
{
  "Rua": String
  "Numero": String
  "Complemento": String
  "Bairro": String
  "Cidade": String
  "Estado": Estado
  "Cep": String
}
```

### Lembrete

```java
{
  "id": Long,
  "titulo": String,
  "descricao": String,
  "data": LocalDateTime,
  "prioridade": Prioridade,
  "categoria": Categoria,
  "concluido": String,
  "usuario": Usuario
}
```

## 📚 Enums das Entidades

### `Estado`
Enum que representa os estados brasileiros.

```
AC, AL, AP, AM, BA, CE, DF, ES, GO, MA,
MT, MS, MG, PA, PB, PR, PE, PI, RJ, RN,
RS, RO, RR, SC, SP, SE, TO
```

### `Categoria`
Enum que representa as categorias de lembretes.

```
SAUDE, LAZER, FAMILIA, PROFISSIONAL, OUTRO
```

### `Prioridade`
Enum que classifica o nível de prioridade do lembrete.

```
ALTA, MODERADA, BAIXA
```

### `Role`
Enum que armazena as roles para usuários.

```
ADMIN, USER
```

---

### Arquitetura em Camadas

O projeto segue o padrão MVC com separação clara de responsabilidades:

1. **Controller** - Recebe requisições HTTP e retorna respostas
2. **Service** - Implementa regras de negócio
3. **Repository** - Acessa e persiste dados
4. **Domain** - Define as entidades do domínio
5. **DTO** - Transfere dados entre camadas
6. **Specifications** - Possui filtros
7. **Mapper** -  Mapeamento dos DTO's para entidades.
8. **Config** -  Configurações da aplicação
9. **Resources** - Possui as templates do thymeleaf e as estilizações css.
    
---

## 👨‍💻 Equipe

### Gustavo Rangel
💼 Estudante de Análise e Desenvolvimento de Sistemas - FIAP  
🔗 [LinkedIn](https://www.linkedin.com/in/gustavoorangel)

### David Rapeckman
💼 Estudante de Análise e Desenvolvimento de Sistemas - FIAP  
🔗 [LinkedIn](https://www.linkedin.com/in/davidrapeckman)

### Luis Felippe Morais
💼 Estudante de Análise e Desenvolvimento de Sistemas - FIAP  
🔗 [LinkedIn](https://www.linkedin.com/in/luis-felippe-morais-das-neves-16219b2b9)

---

## 📝 Licença

Este projeto foi desenvolvido como atividade acadêmica na FIAP.

---
