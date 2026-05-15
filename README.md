# 🏦 SecureBank API

Uma API RESTful robusta para simulação de operações bancárias, desenvolvida com foco em segurança, resiliência e boas práticas de engenharia de software. 

Este projeto foi construído para demonstrar a aplicação de conceitos avançados de backend, incluindo autenticação *stateless* com JWT, proteção de rotas, validação de dados de entrada e tratamento global de exceções.

---

## 📸 Vitrine da API (Swagger UI)

<img width="1460" height="920" alt="SwaggerCompleto" src="https://github.com/user-attachments/assets/8d95e22d-6295-40db-8fd8-98d6da44a2fc" />

<img width="650" height="274" alt="Autorizado" src="https://github.com/user-attachments/assets/eb6c42c2-8180-41f1-9b12-53a4a119bf35" />

---

## 🚀 Funcionalidades Principais

O motor do banco foi projetado para garantir a consistência das transações e a segurança dos dados do usuário:

* **Gerenciamento de Contas:** Criação de contas com verificação de unicidade de CPF e criptografia forte de senhas (BCrypt).
* **Autenticação Segura:** Sistema de Login emitindo tokens JWT (JSON Web Token) com prazo de validade.
* **Operações Financeiras Protegidas:** Endpoints de Depósito, Saque e Transferência blindados pelo Spring Security (requerem token válido no *Header* da requisição).
* **Validações de Domínio:** Bloqueio de transferências para si mesmo, saques com saldo insuficiente e valores negativos/nulos utilizando Bean Validation (`@Valid`).
* **Tratamento Global de Erros:** Padronização das respostas de erro da API (Status 400, 401, 403) utilizando `@RestControllerAdvice`, evitando vazamento de *stack traces* para o cliente.
* **Documentação Viva:** Contrato da API exposto interativamente via Swagger/OpenAPI.

---

## 🛠️ Tecnologias Utilizadas

Este projeto foi desenvolvido utilizando o que há de mais moderno no ecossistema Java:

* **Linguagem:** Java 
* **Framework Principal:** Spring Boot
* **Segurança:** Spring Security + Auth0 `java-jwt`
* **Persistência de Dados:** Spring Data JPA + Hibernate
* **Banco de Dados:** MySQL
* **Documentação:** Springdoc OpenAPI (Swagger)
* **Gerenciamento de Dependências:** Maven

---

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
* Java JDK instalado na sua máquina.
* Um banco de dados MySQL rodando localmente ou via Docker (na porta 3307 ou a que estiver no seu `application.properties`).
* Uma IDE (IntelliJ, Eclipse, VS Code) ou Maven no terminal.

### Passos

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/renangomesdev/securebank.git
