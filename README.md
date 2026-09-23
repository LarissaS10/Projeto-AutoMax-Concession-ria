# 🚗 AutoMax Concessionária

Aplicação desenvolvida com Spring Boot (back-end) e React (front-end) como projeto da disciplina de Engenharia de Software Escalável, evoluindo de um monolito até uma arquitetura de microsserviços.

---

## 📋 Sobre o Projeto

O sistema AutoMax permite o gerenciamento completo de uma concessionária de carros, com cadastro de veículos, clientes, registro de vendas e agendamento de test drives. A aplicação segue uma arquitetura em camadas (Controller → Service → Repository) e aplica conceitos de Domain-Driven Design (DDD) com bounded contexts bem definidos.

---

## 🛠️ Tecnologias Utilizadas

### Back-end (Monolito)
- Java 17
- Spring Boot 3.5
- Spring Data JPA / Hibernate
- Spring Web (Spring MVC)
- H2 Database (arquivo)
- Lombok
- Bean Validation (Jakarta)
- Maven

### Microsserviço — testdrive-service
- Java 17
- Spring Boot 4.1.1
- Spring Cloud Netflix Eureka Client
- H2 Database (arquivo)
- Lombok
- Maven

### Eureka Server
- Java 17
- Spring Boot 4.1.1
- Spring Cloud Netflix Eureka Server

### Front-end
- React 19
- Axios
- JavaScript (ES6+)
- CSS3

---

## ⚙️ Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- Java 17+
- Maven 3.8+
- Node.js 18+ e npm
- IntelliJ IDEA (recomendado para o back-end)
- VS Code (recomendado para o front-end)

---

## 🚀 Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/LarissaS10/Projeto-AutoMax-Concession-ria.git
cd Projeto-AutoMax-Concession-ria/projeto-concessionaria
```

### ⚠️ Ordem obrigatória de inicialização

1️⃣ eureka-server → porta 8761
2️⃣ testdrive-service → porta 8081
3️⃣ backend → porta 8080
4️⃣ front-end → porta 3000


### 2. Rodando o Eureka Server

No IntelliJ, abra o projeto `eureka-server/` e clique em ▶️ em `EurekaServerApplication`.

> Painel disponível em: http://localhost:8761

### 3. Rodando o Microsserviço Test Drive

No IntelliJ, abra o projeto `testdrive-service/` e clique em ▶️ em `TestdriveServiceApplication`.

> API disponível em: http://localhost:8081/api/testdrive

### 4. Rodando o Back-end (Monolito)

No IntelliJ, abra o projeto `backend/` e clique em ▶️ em `BackendApplication`.

> API disponível em: http://localhost:8080/api  
> Console H2: http://localhost:8080/h2-console  
> JDBC URL: `jdbc:h2:file:./data/concessionariadb` | User: `sa` | Password: *(em branco)*

### 5. Rodando o Front-end

No VS Code, abra a pasta `front-end/`, abra o terminal integrado e execute:

```bash
npm install
npm start
```

> Aplicação disponível em: http://localhost:3000
