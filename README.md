# Desafio Hyperativa

Esta aplicação é uma API desenvolvida com **Spring Boot** para cadastro e consulta de números de cartão. A API atende aos seguintes requisitos:

- **Autenticação**: Utiliza JWT para autenticar o usuário.
- **Inserção de Dados**: Permite o cadastro de um único cartão via JSON ou o upload de um arquivo TXT contendo vários números.
- **Consulta de Dados**: Consulta se determinado número de cartão existe no banco e retorna seu identificador único.
- **Segurança**: As informações são tratadas com cuidado devido à sua natureza sensível.

---

## Tecnologias Utilizadas

- Java 17  
- Spring Boot 3.1.1  
- Spring Data JPA  
- Spring Security (autenticação JWT)  
- MySQL (banco de dados)  
- Maven (gerenciamento de dependências e build)  
- Jasypt (para criptografia de dados sensíveis)  

---

## Pré-requisitos

- JDK 17 instalado  
- Maven instalado  
- MySQL instalado e em execução  

---

## Configuração do Banco de Dados

1. Crie um banco de dados MySQL, por exemplo: `desafio_hyperativa`
2. Atualize o arquivo `src/main/resources/application.properties` com suas credenciais e configurações do MySQL:

<pre>spring.datasource.url=jdbc:mysql://localhost:3306/desafio_hyperativa?useSSL=false&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true </pre>

---

## Configuração do JWT

Adicione as seguintes configurações também no `application.properties`:

<pre>jwt.secret=chaveSecretaMuitoForte
jwt.expiration=3600000  # 1 hora em milissegundos</pre>

---

## Como Rodar a Aplicação

### Compilação e Execução

1. No terminal, execute:

`mvn clean install`

2. Para rodar a aplicação:

`mvn spring-boot:run`

A aplicação estará disponível em: http://localhost:8080

---

## Endpoints da API

### 1. Autenticação do Usuário

- URL: /api/auth
- Método: POST
- Request Body:

<pre>{
  "username": "usuarioExemplo",
  "password": "senhaSegura"
}</pre>

- Response:

<pre>{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}</pre>

---

### 2. Inserção de Cartão via JSON

- URL: /api/cards
- Método: POST
- Headers:
  - Authorization: Bearer <JWT_TOKEN>
  - Content-Type: application/json
- Request Body:

<pre>{
  "cardNumber": "4456897999999999"
}</pre>

- Response: Retorna o cartão salvo com seu ID.

---

### 3. Upload de Cartões via Arquivo TXT

- URL: /api/cards/upload
- Método: POST
- Headers:
  - Authorization: Bearer <JWT_TOKEN>
  - Content-Type: multipart/form-data
- Form Data:
  - file: Selecione o arquivo TXT contendo os números de cartão.

- Response: Retorna uma mensagem informando quantos registros foram inseridos com sucesso.

---

### 4. Consulta de Cartão

- URL: /api/cards/{cardNumber}
- Método: GET
- Headers:
  - Authorization: Bearer <JWT_TOKEN>

- Response (cartão existente):

<pre>{
  "id": 1
}</pre>

- Response (cartão inexistente):  
  HTTP 404 com a mensagem: "Cartão não encontrado"

---

## Testes

A aplicação possui testes unitários para os endpoints. Para executá-los:

<pre>mvn test</pre>
