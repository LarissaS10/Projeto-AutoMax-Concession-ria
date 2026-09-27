# AutoMax Concessionária

Aplicação desenvolvida com Spring Boot (back-end) e React (front-end) como projeto da disciplina de Engenharia de Software Escalável, evoluindo de um monolito até uma arquitetura de microsserviços orientada a eventos, conteinerizada com Docker.

---

## Sobre o Projeto

O sistema AutoMax permite o gerenciamento completo de uma concessionária de carros, com cadastro de veículos, clientes, registro de vendas e agendamento de test drives. A aplicação segue uma arquitetura em camadas (Controller → Service → Repository) e aplica conceitos de Domain-Driven Design (DDD) com bounded contexts bem definidos.

Os serviços se comunicam de forma assíncrona pelo RabbitMQ: quando uma venda é realizada no monolito, um evento é publicado na fila `venda.realizada` e consumido pelo microsserviço de test drive.

---

## Tecnologias Utilizadas

### Back-end (Monolito)
- Java 17
- Spring Boot 3.5
- Spring Data JPA / Hibernate
- Spring Web (Spring MVC)
- Spring AMQP (RabbitMQ)
- Spring Boot Actuator
- H2 Database (arquivo)
- Lombok
- Bean Validation (Jakarta)
- Maven

### Microsserviço — testdrive-service
- Java 17
- Spring Boot 4.1.1
- Spring Cloud Netflix Eureka Client
- Spring AMQP (RabbitMQ)
- Spring Boot Actuator
- H2 Database (arquivo)
- Lombok
- Maven

### Eureka Server
- Java 17
- Spring Boot 4.1.1
- Spring Cloud Netflix Eureka Server
- Spring Boot Actuator

### Front-end
- React 19
- Axios
- JavaScript (ES6+)
- CSS3

### Infraestrutura
- RabbitMQ (message broker)
- Docker e Docker Compose
- Kubernetes (manifesto `k8s.yaml`)
- GitHub Actions (integração contínua)

---

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- Java 17+
- Docker Desktop
- Node.js 18+ e npm
- IntelliJ IDEA (recomendado para o back-end)
- VS Code (recomendado para o front-end)

O Maven não precisa estar instalado: cada serviço já inclui o Maven Wrapper (`mvnw`).

---

## Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/LarissaS10/Projeto-AutoMax-Concession-ria.git
cd Projeto-AutoMax-Concession-ria/projeto-concessionaria
```

Há duas formas de executar o back-end: com Docker (recomendada) ou localmente pelo IntelliJ. Não use as duas ao mesmo tempo, pois elas ocupam as mesmas portas.

---

### Opção A — Com Docker (recomendada)

#### A.1. Gere os JARs

Com o terminal dentro de cada pasta (`eureka-server`, `backend` e `testdrive-service`), execute:

```bash
./mvnw clean package
```

Também é possível gerar pelo painel Maven do IntelliJ: Lifecycle → package.

#### A.2. Suba os containers

Com o Docker Desktop aberto, na pasta `projeto-concessionaria`, execute:

```bash
docker compose up --build
```

O Docker Compose sobe os 4 containers na ordem correta: primeiro RabbitMQ e Eureka, e só depois o backend e o testdrive-service, quando os dois primeiros estiverem saudáveis.

Para parar e remover os containers:

```bash
docker compose down
```

Observação: o banco H2 fica dentro de cada container. Ao executar `docker compose down`, os dados cadastrados são apagados.

---

### Opção B — Localmente pelo IntelliJ

#### B.1. Suba o RabbitMQ

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
```

#### B.2. Inicie os serviços nesta ordem

1. `eureka-server` → clique em Run em `EurekaServerApplication`
2. `backend` → clique em Run em `BackendApplication`
3. `testdrive-service` → clique em Run em `TestdriveServiceApplication`

---

### Front-end (nas duas opções)

No VS Code, abra a pasta `front-end/`, abra o terminal integrado e execute:

```bash
npm install
npm start
```

---

## Endereços

| Serviço | Endereço |
|---------|----------|
| Aplicação (React) | http://localhost:3000 |
| Back-end (API) | http://localhost:8080/api |
| Microsserviço Test Drive | http://localhost:8081/api/testdrive |
| Painel do Eureka | http://localhost:8761 |
| Painel do RabbitMQ | http://localhost:15672 (usuário `guest`, senha `guest`) |
| Health check do back-end | http://localhost:8080/actuator/health |
| Health check do test drive | http://localhost:8081/actuator/health |
| Console H2 | http://localhost:8080/h2-console |

Console H2: JDBC URL `jdbc:h2:file:./data/concessionariadb` | User `sa` | Password em branco.

---

## Integração Contínua

O workflow `.github/workflows/ci.yml` é executado automaticamente a cada push ou pull request na branch `main`. Ele compila os três serviços e executa os testes automatizados do backend e do testdrive-service. Os resultados podem ser acompanhados na aba **Actions** do repositório.

Para rodar os testes localmente, dentro da pasta do serviço:

```bash
./mvnw test
```

---

## Estrutura do Projeto

```
Projeto-AutoMax-Concession-ria/
├── .github/workflows/ci.yml     # pipeline de CI
├── README.md
└── projeto-concessionaria/
    ├── backend/                  # monolito + Dockerfile
    ├── eureka-server/            # servidor de descoberta + Dockerfile
    ├── testdrive-service/        # microsserviço + Dockerfile
    ├── front-end/                # React
    ├── docker-compose.yml        # orquestração dos containers
    └── k8s.yaml                  # manifesto Kubernetes
```
