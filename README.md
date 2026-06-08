🚗 AutoMax Concessionária

Aplicação monolítica desenvolvida com Spring Boot (back-end) e React (front-end) como primeira entrega do projeto de Engenharia de Software Escalável.

📋 Sobre o Projeto

O sistema AutoMax permite o gerenciamento completo de uma concessionária de carros, com cadastro de veículos, clientes e registro de vendas. A aplicação segue uma arquitetura em camadas (Controller → Service → Repository) e aplica conceitos de Domain-Driven Design (DDD) com bounded contexts bem definidos.

🛠️ Tecnologias Utilizadas

Back-end

Java 17,
Spring Boot 3.5,
Spring Data JPA / Hibernate,
Spring Web (Spring MVC),
H2 Database (in-memory),
Lombok,
Bean Validation (Jakarta),
Maven.

Front-end

React 19,
Axios,
JavaScript,
CSS3.

⚙️ Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

Java 17+
Maven 3.8+
Node.js 18+ e npm

🚀 Como Executar

1. Clone o repositório

cd projeto-concessionaria

3. Rodando o Back-end

No intellij.
bash cd backend,
mvn spring-boot:run,
A API estará disponível em: http://localhost:8080/api
Console do banco H2: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:concessionariadb | User: sa | Password: 123

3. Rodando o Front-end

Em outro terminal: indico Visual Studio Code.
bash cd frontend,
npm install,
npm start,
A aplicação estará disponível em: http://localhost:3000
