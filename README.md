# 🚀 Microsserviço de Autenticação (`ms-autenticacao`)

Este repositório contém o microsserviço de autenticação, parte fundamental de uma arquitetura baseada em microsserviços
para um banco digital. Ele é responsável por gerenciar o ciclo de vida da autenticação, incluindo login, gestão de 
usuários e emissão de tokens JWT para comunicação segura entre os serviços.

---

## 💡 Sobre o Projeto

O `ms-autenticacao` é construído com **Spring Boot** e segue os princípios da **Arquitetura Hexagonal (Ports and 
Adapters)**, garantindo uma forte separação de preocupações e alta testabilidade. A segurança é implementada utilizando
**Spring Security** com foco em autenticação baseada em **JWT (JSON Web Tokens)** para uma comunicação stateless 
e escalável.

---

## ⚙️ Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Security** (para autenticação e autorização)
* **JJWT** (para geração e validação de JWTs)
* **Maven** (para gerenciamento de dependências)
* **JUnit 5** e **Mockito** (para testes)

---

## 🏗️ Estrutura do Projeto (Arquitetura Hexagonal)

O projeto está organizado em camadas claras, seguindo a arquitetura hexagonal para isolar a lógica de negócio 
(domínio) da infraestrutura e dos frameworks externos.

```bash
src/main/java/br/com/bancodigital/msautenticacao/
├── application/
│   └── usecase/
│   └── service/
│       └── AuthService.java
│
├── domain/
│   └── model/
│       └── User.java
│       └── AuthenticatedUser.java
│   └── exception/
│   └── port/
│       └── in/
│           └── LoginUseCase.java
│       └── out/
│           └── UserRepositoryPort.java
│
└── adapter/
    ├── in/
    │   ├── web/
    │   │   ├── controller/
    │   │   │   └── AuthController.java
    │   │   ├── security/
    │   │   │   └── SecurityConfig.java
    │   │   │   └── JwtService.java
    │   │   ├── dto/
    │   │   │   └── LoginRequest.java
    │   │   │   └── LoginResponse.java
    │   │   ├── mapper/
    │   │   │   └── LoginMapper.java
    │   │   └── exception/
    │   │       └── handler/
    │   │           └── GlobalExceptionHandler.java
    │   └── cli/ (exemplo)
    │
    └── out/
        ├── persistence/
        │   └── repository/
        │   └── entity/
        ├── messaging/
        └── security/
            └── CustomUserDetailsService.java
```

---

## 🔒 Configuração de Segurança (Spring Security & JWT)

O microsserviço implementa uma configuração de segurança robusta:

* **`PasswordEncoder` (BCrypt):** Senhas são armazenadas com hash seguro usando BCrypt.
* **Usuários em Memória (para Desenvolvimento):** Para facilitar o desenvolvimento e testes iniciais, o serviço vem configurado com usuários em memória com diferentes papéis:
    * `username: cliente01` / `password: senhaCliente123` (Role: `CLIENTE`)
    * `username: gerente01` / `password: senhaGerente123` (Role: `GERENTE`)
    * `username: supervisor01` / `password: senhaSupervisor123` (Roles: `SUPERVISOR`, `GERENTE`)
* **Políticas de Autorização:** Endpoints são protegidos com base nos papéis dos usuários (`hasRole`, `hasAnyRole`).
* **JWT (JSON Web Tokens):** Após o login bem-sucedido, um JWT é gerado e retornado ao cliente. Este token é a base para a autenticação de requisições subsequentes para este e outros microsserviços.

---

## 🚀 Como Rodar o Projeto

1.  **Pré-requisitos:**
    * Java Development Kit (JDK) 17 ou superior.
    * Maven 3.6+
2.  **Clonar o repositório:**
    ```bash
    git clone https://github.com/edbsjr/ms-autenticacao-banco-digital
    cd ms-autenticacao
    ```
3.  **Configurar `application.properties` (ou `application.yml`):**
    Crie um arquivo `src/main/resources/application.properties` e adicione as seguintes propriedades para a configuração do JWT:
    ```properties
    # Chave secreta JWT (DEVE SER LONGA, ÚNICA E SEGURA EM PRODUÇÃO)
    application.security.jwt.secret-key=SuaChaveSecretaMuitoLongaEseguraAquiParaAssinarTokensJWT12345!@#$%
    # Tempo de expiração do JWT em milissegundos (ex: 86400000 ms = 24 horas)
    application.security.jwt.expiration=86400000
    ```
    **Importante:** No ambiente de produção, essas chaves devem ser gerenciadas de forma segura (variáveis de ambiente, HashiCorp Vault, etc.) e nunca hardcoded ou expostas no código.
4.  **Compilar e Rodar:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    O serviço estará disponível em `http://localhost:8080` (porta padrão do Spring Boot, a menos que configurada de forma diferente).

---

## 🧪 Testes

Os testes são configurados com JUnit 5. Para rodar todos os testes:

```bash
mvn test
```
## 🔑 Endpoints da API

> ⚠️ Esta seção será atualizada à medida que novos endpoints forem implementados.

### 🔐 Autenticação

**`POST /auth/login`**

- **Descrição:** Autentica um usuário e, se bem-sucedido, retorna um JWT.
- **Request Body:** `LoginRequest` (JSON com `username`, `password`)
- **Response Body:** `LoginResponse` (JSON com `token`)
- **Status Codes:**
    - `200 OK` – Autenticação bem-sucedida.
    - `400 Bad Request` – Credenciais inválidas ou erro de validação.
    - `401 Unauthorized` – Falha na autenticação (usuário ou senha incorretos).
    - `500 Internal Server Error` – Erro inesperado no servidor.

---

### 📌 Endpoints Futuros

- `POST /auth/trocar-senha` – Trocar a senha do usuário autenticado.
- `POST /auth/resetar-senha` – Iniciar processo de recuperação de senha.
- `GET /api/clientes/profile` – Recuperar perfil do cliente autenticado (protegido por `ROLE_CLIENTE`).

---

## 🤝 Contribuição

Contribuições são bem-vindas! Se você tiver sugestões ou encontrar problemas, sinta-se à vontade para abrir uma **issue** ou enviar um **pull request**.

---

## 📄 Licença

Este projeto está licenciado sob a **Licença MIT**.

