# 🚀 Microsserviço de Autenticação (`ms-autenticacao`)

Este repositório contém o **Microsserviço de Autenticação**, componente central de uma arquitetura de Banco Digital 
baseada em microsserviços. O serviço é responsável pelo ciclo de vida de identidade, abrangendo desde o registro e
gestão de usuários até a emissão de **tokens JWT (JSON Web Tokens)** para garantir a comunicação segura e confiável 
entre os serviços da malha.

---

## 💡 Sobre o Projeto

O `ms-autenticacao` foi desenvolvido com **Spring Boot 3** e fundamentado nos princípios da **Arquitetura Hexagonal 
(Ports and Adapters)**. Essa abordagem garante que a lógica de negócio (Core) permaneça isolada e independente de 
tecnologias externas, como bancos de dados ou protocolos de comunicação, facilitando a testabilidade e a manutenção 
a longo prazo.

A segurança é gerida pelo **Spring Security**, configurado para operar de forma stateless, otimizando a escalabilidade
do serviço em ambientes de nuvem.

Este microsserviço atua como o **Provedor de Identidade (Identity Provider)** central do ecossistema. Ele é o ponto de
entrada que garante a integridade de todo o sistema de Banco Digital, emitindo credenciais que serão consumidas e
validadas por outros serviços futuros, como o `ms-contas` (gestão de contas correntes) e o `ms-transacoes` 
(processamento de Pix e transferências).

---

## ⚙️ Stack Tecnológica e Ferramental

### **Core & Frameworks**

* **Java 21 (LTS):** Utilização das últimas funcionalidades da linguagem, como Records para DTOs imutáveis e melhor
  performance da JVM.

* **Spring Boot 3.5.3:** Base do projeto, facilitando a configuração automática e o gerenciamento de dependências via
  ecossistema Spring.

* **Jakarta Bean Validation:** Implementação de validação declarativa nas bordas da aplicação (DTOs), garantindo que
apenas dados íntegros cheguem à lógica de negócio.

### **Segurança & Identidade**
* **Spring Security:** Framework utilizado para blindar os endpoints e gerenciar o fluxo de autenticação e autorização
de forma centralizada.

* **JJWT (Java JWT):** Biblioteca especializada para a criação, assinatura e parsing de tokens JWT, garantindo a 
integridade da comunicação Stateless.

### **Documentação & Interface**
* **SpringDoc OpenAPI (Swagger):** Implementação de documentação interativa baseada na especificação OpenAPI 3, 
permitindo o teste dos endpoints diretamente pelo navegador.

### **Persistência & Dados**
* **H2 Database:** Banco de dados SQL em memória, configurado para agilizar o ciclo de desenvolvimento e permitir a 
execução imediata do projeto sem dependências externas de infraestrutura.

* **Spring Data JDBC:** Abstração de acesso a dados que segue princípios de Domain-Driven Design (DDD), mantendo a 
persistência simples e eficiente.

### **Qualidade & Testes**
* **JUnit 5 & Mockito:** Ferramentas para criação de suítes de testes automatizados, garantindo que as regras de negócio
permaneçam protegidas contra regressões.

* **Jacoco (Java Code Coverage):** Plugin utilizado para medir a cobertura de testes do código, assegurando métricas de 
qualidade e confiabilidade para o projeto.


---

## 🏗️ Estrutura do Projeto (Arquitetura Hexagonal)

O projeto está organizado em camadas claras, seguindo a arquitetura hexagonal para isolar a lógica de negócio 
(domínio) da infraestrutura e dos frameworks externos.
+---src
|   +---main
|   |   +---java
|   |   |   \---br
|   |   |       \---com
|   |   |           \---bancodigital
|   |   |               \---msautenticacao
|   |   |                   |   MsAutenticacaoApplication.java
|   |   |                   |
|   |   |                   
|   |   |                   |
|   |   |                   +---application
|   |   |                   |   +---port
|   |   |                   |   |   +---in
|   |   |                   |   |   |       LoginUseCase.java
|   |   |                   |   |   |       RegisterUserUseCase.java
|   |   |                   |   |   |
|   |   |                   |   |   \---out
|   |   |                   |   |           TokenProviderPort.java
|   |   |                   |   |           UserRepositoryPort.java
|   |   |                   |   |
|   |   |                   |   +---service
|   |   |                   |   |       AuthService.java
|   |   |                   |   |       RegisterUserService.java
|   |   |                   |   |
|   |   |                   |   \---usecase
|   |   |                   |       \---command
|   |   |                   |               LoginCommand.java
|   |   |                   |               RegisterUserCommand.java
|   |   |                   |
|   |   |                   \---domain
|   |   |                       +---exception
|   |   |                       |   |   AuthenticationException.java
|   |   |                       |   |
|   |   |                       |   \---errorcode
|   |   |                       |           AuthenticationErrorCode.java
|   |   |                       |
|   |   |                       \---model
|   |   |                           |   AuthenticatedUser.java
|   |   |                           |   User.java
|   |   |                           |
|   |   |                           \---enums
|   |   |                                   UserRole.java
|   |   |                                   UserStatus.java
|   |   |
|   |   \---resources
|   |       |   application.properties
|   |       |   data.sql
|   |       |   logback.xml
|   |       |   schema.sql
|   |       |
|   |       +---static
|   |       \---templates
|   \---test
|       \---java
|           \---br
|               \---com
|                   \---bancodigital
|                       \---msautenticacao
|                           |   MsAutenticacaoApplicationTests.java
|                           |
|                           +---adapter
|                           |   +---in
|                           |   |   +---security
|                           |   |   |       JwtServiceTest.java
|                           |   |   |
|                           |   |   \---web
|                           |   |       +---controller
|                           |   |       |       AuthControllerTest.java
|                           |   |       |
|                           |   |       \---exception
|                           |   |               GlobalExceptionHandlerTest.java
|                           |   |
|                           |   \---out
|                           |       \---persistence
|                           |               JdbcUserRepositoryTest.java
|                           |
|                           \---application
|                               \---service
|                                       AuthServiceTest.java
|                                       RegisterUserServiceTest.java

```bash
src/main/java/br/com/bancodigital/msautenticacao/
+---src
|   +---main
|   |   +---java
|   |   |   \---br
|   |   |       \---com
|   |   |           \---bancodigital
|   |   |               \---msautenticacao
|   |   |                   |   MsAutenticacaoApplication.java
|   |   |                   |
|   |   |                   +---adapter
|   |   |                   |   +---in
|   |   |                   |   |   +---security
|   |   |                   |   |   |       CustomUserDetails.java
|   |   |                   |   |   |       CustomUserDetailsService.java
|   |   |                   |   |   |       SecurityConfig.java
|   |   |                   |   |   |
|   |   |                   |   |   \---web
|   |   |                   |   |       +---controller
|   |   |                   |   |       |       AuthController.java
|   |   |                   |   |       |
|   |   |                   |   |       +---dto
|   |   |                   |   |       |       ErrorResponse.java
|   |   |                   |   |       |       LoginRequest.java
|   |   |                   |   |       |       LoginResponse.java
|   |   |                   |   |       |       RegisterRequest.java
|   |   |                   |   |       |
|   |   |                   |   |       +---exception
|   |   |                   |   |       |   \---handler
|   |   |                   |   |       |           GlobalExceptionHandler.java
|   |   |                   |   |       |
|   |   |                   |   |       \---mapper
|   |   |                   |   |               ErrorMapper.java
|   |   |                   |   |               LoginMapper.java
|   |   |                   |   |               RegisterMapper.java
|   |   |                   |   |
|   |   |                   |   \---out
|   |   |                   |       +---persistence
|   |   |                   |       |       JdbcUserRepository.java
|   |   |                   |       |       UserRowMapper.java
|   |   |                   |       |
|   |   |                   |       \---security
|   |   |                   |               JwtService.java
|   |   |                   |
|   |   |                   +---application
|   |   |                   |   +---port
|   |   |                   |   |   +---in
|   |   |                   |   |   |       LoginUseCase.java
|   |   |                   |   |   |       RegisterUserUseCase.java
|   |   |                   |   |   |
|   |   |                   |   |   \---out
|   |   |                   |   |           TokenProviderPort.java
|   |   |                   |   |           UserRepositoryPort.java
|   |   |                   |   |
|   |   |                   |   +---service
|   |   |                   |   |       AuthService.java
|   |   |                   |   |       RegisterUserService.java
|   |   |                   |   |
|   |   |                   |   \---usecase
|   |   |                   |       \---command
|   |   |                   |               LoginCommand.java
|   |   |                   |               RegisterUserCommand.java
|   |   |                   |
|   |   |                   \---domain
|   |   |                       +---exception
|   |   |                       |   |   AuthenticationException.java
|   |   |                       |   |
|   |   |                       |   \---errorcode
|   |   |                       |           AuthenticationErrorCode.java
|   |   |                       |
|   |   |                       \---model
|   |   |                           |   AuthenticatedUser.java
|   |   |                           |   User.java
|   |   |                           |
|   |   |                           \---enums
|   |   |                                   UserRole.java
|   |   |                                   UserStatus.java
|   |   |
|   |   \---resources
|   |       |   application.properties
|   |       |   data.sql
|   |       |   logback.xml
|   |       |   schema.sql
|   |       |
|   |       +---static
|   |       \---templates
|   \---test
|       \---java
|           \---br
|               \---com
|                   \---bancodigital
|                       \---msautenticacao
|                           |   MsAutenticacaoApplicationTests.java
|                           |
|                           +---adapter
|                           |   +---in
|                           |   |   +---security
|                           |   |   |       JwtServiceTest.java
|                           |   |   |
|                           |   |   \---web
|                           |   |       +---controller
|                           |   |       |       AuthControllerTest.java
|                           |   |       |
|                           |   |       \---exception
|                           |   |               GlobalExceptionHandlerTest.java
|                           |   |
|                           |   \---out
|                           |       \---persistence
|                           |               JdbcUserRepositoryTest.java
|                           |
|                           \---application
|                               \---service
|                                       AuthServiceTest.java
|                                       RegisterUserServiceTest.java
|

```

---

## 🔒 Segurança e Controle de Acesso

O microsserviço implementa uma camada de segurança robusta utilizando **Spring Security 3** e **JWT**, focada em 
proteção de dados e controle granular de acesso.

* **Criptografia de Senhas (BCrypt):** Nenhuma senha é armazenada em texto plano. Utilizamos o algoritmo **BCrypt**, que
inclui salting automático para proteger contra ataques de dicionário e tabelas de arco-íris.

* **Gestão de Identidades (H2 Persistence):** O serviço utiliza um banco de dados H2 para persistência. Para facilitar 
testes e avaliações, o arquivo `data.sql` popula o banco automaticamente com os seguintes perfis:

    * **ADMIN:** Acesso total à gestão do sistema.

    * **GERENTE:** Acesso a funcionalidades administrativas e de suporte.

    * **CLIENTE:** Acesso restrito às operações de usuário final.

* **Políticas de Autorização:** As rotas são protegidas via **Role-Based Access Control (RBAC)**. O acesso é validado 
através das autoridades do usuário no momento da requisição (`hasRole`, `hasAnyRole`).

* **Tokens JWT (Stateless):** A autenticação é totalmente baseada em tokens. Após o login, o servidor emite um JWT 
* assinado. Este token deve ser enviado no cabeçalho `Authorization` de todas as requisições subsequentes, permitindo 
* que a aplicação seja escalável e não dependa de estado no servidor (Stateless).

---

## 🚀 Como Rodar o Projeto

1.  **Pré-requisitos:**
    * **Java Development Kit (JDK) 21** ou superior.
    * **Maven 3.9+** (recomendado)
2.  **Clonar o repositório:**
    ```bash
    git clone https://github.com/edbsjr/ms-autenticacao-banco-digital
    cd ms-autenticacao
    ```
3.  **Configuração do Ambiente**
    O projeto já contém um arquivo `application.properties` funcional para desenvolvimento em `src/main/resources/`. 
No entanto, para o funcionamento do **JWT**, certifique-se de que as propriedades de segurança estão configuradas:
    ```properties
    # Chave secreta JWT (DEVE SER LONGA, ÚNICA E SEGURA EM PRODUÇÃO)
    application.security.jwt.secret-key=SuaChaveSecretaMuitoLongaEAleatoriaEseguraAquiParaAssinarTokensJWT12345!@#$%
    # Tempo de expiração do JWT em milissegundos (ex: 86400000 ms = 24 horas)
    application.security.jwt.expiration=86400000
    ```
    ⚠️**Aviso de Segurança:** Em ambientes de produção, nunca exponha essas chaves no código. Utilize variáveis de 
ambiente ou serviços de Secret Management (como Vault ou AWS Secrets Manager).
4.  **Compilar e Rodar:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    O serviço estará disponível em `http://localhost:8080`

---

# 🚀 Microsserviço de Autenticação (`ms-autenticacao`)

Este repositório contém o **Microsserviço de Autenticação**, componente central de uma arquitetura de Banco Digital
baseada em microsserviços. O serviço é responsável pelo ciclo de vida de identidade, abrangendo desde o registro e
gestão de usuários até a emissão de **tokens JWT (JSON Web Tokens)** para garantir a comunicação segura e confiável
entre os serviços da malha.

---

## 💡 Sobre o Projeto

O `ms-autenticacao` foi desenvolvido com **Spring Boot 3** e fundamentado nos princípios da **Arquitetura Hexagonal
(Ports and Adapters)**. Essa abordagem garante que a lógica de negócio (Core) permaneça isolada e independente de
tecnologias externas, como bancos de dados ou protocolos de comunicação, facilitando a testabilidade e a manutenção
a longo prazo.

A segurança é gerida pelo **Spring Security**, configurado para operar de forma stateless, otimizando a escalabilidade
do serviço em ambientes de nuvem.

Este microsserviço atua como o **Provedor de Identidade (Identity Provider)** central do ecossistema. Ele é o ponto de
entrada que garante a integridade de todo o sistema de Banco Digital, emitindo credenciais que serão consumidas e
validadas por outros serviços futuros, como o `ms-contas` (gestão de contas correntes) e o `ms-transacoes`
(processamento de Pix e transferências).

---
## ⚙️ Stack Tecnológica e Ferramental

### **Core & Frameworks**

* **Java 21 (LTS)**   
  Utilização das últimas funcionalidades da linguagem, como Records para DTOs imutáveis e melhor
  performance da JVM.

* **Spring Boot 3.5.3**   
  Base do projeto, facilitando a configuração automática e o gerenciamento de dependências via
  ecossistema Spring.

* **Jakarta Bean Validation**   
  Implementação de validação declarativa nas bordas da aplicação (DTOs), garantindo que
  apenas dados íntegros cheguem à lógica de negócio.

### **Segurança & Identidade**
* **Spring Security**
  Framework utilizado para blindar os endpoints e gerenciar o fluxo de autenticação e autorização
  de forma centralizada.

* **JJWT (Java JWT)**   
  Biblioteca especializada para a criação, assinatura e parsing de tokens JWT, garantindo a
  integridade da comunicação Stateless.

### **Documentação & Interface**
* **SpringDoc OpenAPI (Swagger)**   
  Implementação de documentação interativa baseada na especificação OpenAPI 3,
  permitindo o teste dos endpoints diretamente pelo navegador.

### **Persistência & Dados**
* **H2 Database**   
  Banco de dados SQL em memória, configurado para agilizar o ciclo de desenvolvimento e permitir a
  execução imediata do projeto sem dependências externas de infraestrutura.

* **Spring Data JDBC**   
  Abstração de acesso a dados que segue princípios de Domain-Driven Design (DDD), mantendo a
  persistência simples e eficiente.

### **Qualidade & Testes**
* **JUnit 5 & Mockito**   
  Ferramentas para criação de suítes de testes automatizados, garantindo que as regras de negócio
  permaneçam protegidas contra regressões.

* **Jacoco (Java Code Coverage)**   
  Plugin utilizado para medir a cobertura de testes do código, assegurando métricas de
  qualidade e confiabilidade para o projeto.

---

## 🏗️ Estrutura do Projeto (Arquitetura Hexagonal)

O projeto está organizado em camadas bem definidas, seguindo os princípios da **Arquitetura Hexagonal**, isolando domínio, aplicação e infraestrutura.

```text
src
├── main
│   ├── java
│   │   └── br/com/bancodigital/msautenticacao
│   │       ├── MsAutenticacaoApplication.java
│   │       ├── adapter
│   │       │   ├── in
│   │       │   │   ├── security
│   │       │   │   └── web
│   │       │   └── out
│   │       │       ├── persistence
│   │       │       └── security
│   │       ├── application
│   │       │   ├── port
│   │       │   │   ├── in
│   │       │   │   └── out
│   │       │   ├── service
│   │       │   └── usecase
│   │       └── domain
│   │           ├── exception
│   │           └── model
│   └── resources
│       ├── application.properties
│       ├── data.sql
│       ├── schema.sql
│       └── logback.xml
└── test
    └── java
        └── br/com/bancodigital/msautenticacao
            ├── adapter
            └── application
```
---

## 🔒 Segurança e Controle de Acesso

O microsserviço implementa uma camada de segurança robusta utilizando **Spring Security 3** e **JWT**, focada em
proteção de dados e controle granular de acesso.

* **Criptografia de Senhas (BCrypt):** Nenhuma senha é armazenada em texto plano. Utilizamos o algoritmo **BCrypt**, que
  inclui salting automático para proteger contra ataques de dicionário e tabelas de arco-íris.

* **Gestão de Identidades (H2 Persistence):** O serviço utiliza um banco de dados H2 para persistência. Para facilitar
  testes e avaliações, o arquivo `data.sql` popula o banco automaticamente com os seguintes perfis:

    * **ADMIN:** Acesso total à gestão do sistema.

    * **GERENTE:** Acesso a funcionalidades administrativas e de suporte.

    * **CLIENTE:** Acesso restrito às operações de usuário final.

* **Políticas de Autorização:** As rotas são protegidas via **Role-Based Access Control (RBAC)**. O acesso é validado
  através das autoridades do usuário no momento da requisição (`hasRole`, `hasAnyRole`).

* **Tokens JWT (Stateless):** A autenticação é totalmente baseada em tokens. Após o login, o servidor emite um JWT
* assinado. Este token deve ser enviado no cabeçalho `Authorization` de todas as requisições subsequentes, permitindo
* que a aplicação seja escalável e não dependa de estado no servidor (Stateless).

---

## 🚀 Como Rodar o Projeto

1.  ✅**Pré-requisitos:**
    * **Java Development Kit (JDK) 21** ou superior.
    * **Maven 3.9+** (recomendado)
2.  📥**Clonar o repositório:**
    ```bash
    git clone https://github.com/edbsjr/ms-autenticacao-banco-digital
    cd ms-autenticacao
    ```
3.  ⚙️**Configuração do Ambiente**
    O projeto já contém um arquivo `application.properties` funcional para desenvolvimento em `src/main/resources/`.
    No entanto, para o funcionamento do **JWT**, certifique-se de que as propriedades de segurança estão configuradas:
    ```properties
    # Chave secreta JWT (DEVE SER LONGA, ÚNICA E SEGURA EM PRODUÇÃO)
    application.security.jwt.secret-key=SuaChaveSecretaMuitoLongaEAleatoriaEseguraAquiParaAssinarTokensJWT12345!@#$%
    # Tempo de expiração do JWT em milissegundos (ex: 86400000 ms = 24 horas)
    application.security.jwt.expiration=86400000
    ```
    ⚠️**Aviso de Segurança:** Em ambientes de produção, nunca exponha essas chaves no código. Utilize variáveis de
    ambiente ou serviços de Secret Management (como Vault ou AWS Secrets Manager).
4.  **Compilar e Rodar:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    O serviço estará disponível em `http://localhost:8080`

---

## 🧪 Suíte de Testes e Qualidade

O projeto adota uma estratégia de testes em pirâmide, garantindo a integridade em todas as camadas da **Arquitetura Hexagonal**:

1. **Estratégia de Execução**
   Para rodar a suíte completa de testes unitários e de integração, utilize o comando:

```Bash
mvn test
```

2. **Divisão por Camadas**
    * **Domain:** Testes unitários focados nas regras de negócio puras, garantindo que o núcleo da aplicação seja
      independente de frameworks.

    * **Application:** Testes de Use Cases utilizando Mockito para simular as portas (Ports) de saída.

    * **Adapters:** Testes de integração para validar o comportamento dos Controllers (Web) e a persistência no banco H2.

3. **Cobertura de Código (Jacoco)**
   Utilizamos o Jacoco para monitorar a eficácia dos testes. Após a execução do comando mvn test, o relatório detalhado
   de cobertura (HTML) é gerado automaticamente e pode ser visualizado em:

📊`target/site/jacoco/index.html`

**Nota de Qualidade:** O foco não é apenas atingir "100% de cobertura", mas garantir que os caminhos críticos e as
exceções (como falhas de login e dados inválidos) estejam devidamente protegidos contra regressões.

## 🔑 Endpoints da API - 📖 Documentação Interativa (Swagger)

A API utiliza **Swagger/OpenAPI 3** para fornecer uma documentação interativa e fácil de consumir. Com a aplicação
rodando, você pode testar todos os endpoints diretamente pelo navegador.

🔗 **Acesse aqui:** 👉 [Swagger UI - Ms-Autenticacao](http://localhost:8080/swagger-ui/index.html)

> **Dica:** Através dessa interface, é possível simular requisições de Login e Registro sem a necessidade de ferramentas
> externas como Postman ou Insomnia.

[ADICIONAR PRINT DA TELA AQUI MOSTRANDO O SWAGGER PRONTO]

### 🔐 Autenticação

**`POST /auth/register`**

- **Descrição:** Registra um novo usuário no sistema. A senha é automaticamente criptografada antes da persistência.
- **Corpo da Requisição:** `RegisterRequest` (JSON com `username`, `password` e `role`)
- **Principais Status Codes:**
    - `201 CREATED` – Registro realizado com sucesso.
    - `400 Bad Request` – Erro de validação (ex: senha menor que 8 caracteres ou login vazio).
    - `409 Conflict` – Usuário já cadastrado no sistema.

**`POST /auth/login`**

- **Descrição:** Valida as credenciais e retorna um token JWT para acesso aos recursos protegidos.
- **Corpo da Requisição:** `LoginRequest` (JSON com `username`, `password`)
- **Principais Status Codes:**
    - `200 OK` – Autenticação bem-sucedida.
    - `400 Bad Request` – Formato de JSON inválido ou campos obrigatórios ausentes.
    - `401 Unauthorized` – Usuário ou senha incorretos.

---

### 📌 Endpoints Futuros

- Gestão de Senha: Implementação de endpoints para troca e recuperação de senha.

- Perfil do Usuário: Criação do endpoint `GET /auth/me` para retornar dados do usuário autenticado.

- Persistência Externa: Migração do banco de dados em memória (H2) para uma instância gerenciada de MySQL/PostgreSQL.

---

## 🤝 Contribuição

Este é um projeto de portfólio para fins de estudo. Sinta-se à vontade para abrir uma Issue ou enviar um Pull Request se encontrar algum erro ou tiver sugestões de melhoria.

---

## ✉️ Contato

Eduardo Batista - [Seu LinkedIn] - [Seu E-mail]

---

## 📄 Licença

Este projeto está licenciado sob a **Licença MIT**.

