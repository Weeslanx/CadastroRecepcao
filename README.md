
# **Cadastro de Recepção**

## **📌 Sobre o Projeto**
O **Cadastro de Recepção** é uma aplicação completa para gerenciamento de visitantes em recepções empresariais e institucionais. O sistema oferece funcionalidades robustas para cadastro, consulta, atualização e exclusão de registros, com suporte a filtros e exportação de dados.

Além disso, conta com autenticação segura via JWT, interface responsiva e documentação interativa da API. A solução foi projetada para ser escalável, eficiente e de fácil integração com sistemas corporativos.

---

## **✨ Recursos e Funcionalidades**

### **1. Gerenciamento Completo de Visitantes**
- Cadastro, consulta, atualização e exclusão de registros.
- Busca avançada e filtragem por nome, data e categoria.
- Relatórios detalhados com exportação para CSV.

### **2. Segurança Avançada**
- Autenticação via **JWT** para acesso seguro.
- Controle de permissões para administradores e usuários.

### **3. Interface Moderna**
- Interface desenvolvida com **Thymeleaf** para páginas dinâmicas.
- Responsiva e adaptada para uso em desktops, tablets e smartphones.

### **4. Documentação da API**
- Endpoints documentados com **Swagger**, permitindo testes diretos no navegador.

---

## **🚀 Tecnologias Utilizadas**
- **Backend**: Java 17, Spring Boot, Spring Data JPA (Hibernate), Spring Security.
- **Frontend**: Thymeleaf, HTML5, CSS3, Bootstrap, JavaScript.
- **Banco de Dados**: MySQL.
- **Gerenciamento de Dependências**: Maven.
- **Ferramentas**: Docker (containerização), Swagger (documentação de API), Postman (testes de API).

---

## **🔧 Como Configurar e Executar o Projeto**

### **Pré-requisitos**
Certifique-se de ter as seguintes ferramentas instaladas:
- **Java 17** ou superior.
- **Maven 3.8** ou superior.
- **MySQL 8.0** ou superior.
- **Docker** (opcional, para deploy em contêiner).

### **1. Clone o repositório**
Clone o projeto do GitHub para o seu ambiente local:
```bash
git clone https://github.com/seu-usuario/cadastro-recepcao.git
```

### **2. Configure o Banco de Dados**
- Crie um banco de dados no MySQL com o nome `cadastro_recepcao`.
- No diretório do projeto, localize o arquivo `application.properties` dentro de `src/main/resources`.
- Atualize as configurações do arquivo para refletir suas credenciais:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/cadastro_recepcao
  spring.datasource.username=SEU_USUARIO
  spring.datasource.password=SUA_SENHA
  ```
  > **Atenção:** Substitua `SEU_USUARIO` e `SUA_SENHA` pelas credenciais corretas do seu ambiente MySQL.

### **3. Compile e Execute o Projeto**
Use o Maven para compilar e executar o projeto:
```bash
mvn spring-boot:run
```

## **📂 Estrutura do Projeto**
```
CadastroRecepcao
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.servicos.cadastro
│   │   │   │   ├── controller
│   │   │   │   ├── service
│   │   │   │   ├── model
│   │   │   │   ├── repository
│   │   │   │   └── security
│   │   ├── resources
│   │   │   ├── templates (HTML para o Thymeleaf)
│   │   │   ├── static (CSS, JS, imagens)
│   │   │   └── application.properties
│   ├── test
│   │   └── java (testes unitários e de integração)
├── pom.xml
```

---

## **📜 Endpoints Principais**
- **POST `/api/visitantes`**: Cadastrar novo visitante.
- **GET `/api/visitantes`**: Listar todos os visitantes.
- **PUT `/api/visitantes/{id}`**: Atualizar dados de um visitante.
- **DELETE `/api/visitantes/{id}`**: Remover um visitante.

---

## **📋 Observações Importantes**
- Certifique-se de que o banco de dados esteja em execução antes de iniciar o projeto.
- O arquivo `application.properties` deve conter as credenciais corretas para acesso ao banco de dados.

---


