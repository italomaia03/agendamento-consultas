# Aplicação de Agendamento de Consultas Médicas

Este é o README para a aplicação de agendamento de consultas médicas, um sistema de back-end desenvolvido em Java com o framework Spring Boot.

## Tipos de usuários e suas permissões

Dentro da aplicação de agendamento de consultas médicas, diferentes entidades possuem diferentes conjuntos de permissões:

### Pacientes:

- **Registrar-se:** Os pacientes têm permissão para se registrar na aplicação, criando uma conta para acessar os recursos disponíveis.

- **Marcar uma consulta:** Os pacientes podem agendar consultas com os médicos disponíveis na plataforma.

- **Listar médicos:** Os pacientes podem visualizar a lista de médicos disponíveis para marcar consultas.

- **Ver os próprios agendamentos:** Os pacientes têm permissão para visualizar as consultas que eles próprios agendaram.

- **Atualizar os próprios dados:** Os pacientes podem atualizar as informações pessoais associadas à sua conta.

- **Deletar a própria conta:** Os pacientes têm a capacidade de excluir sua própria conta da aplicação.

### Médicos:

- **Listar os próprios agendamentos:** Os médicos podem visualizar a lista de consultas agendadas com eles.

- **Atualizar status dos agendamentos:** Os médicos têm permissão para atualizar o status das consultas agendadas com eles, como confirmar, cancelar ou reagendar.

- **Atualizar os próprios dados cadastrais:** Os médicos podem atualizar as informações pessoais associadas à sua conta na aplicação.

### Admin:

- **Todas as permissões das anteriores:** Os administradores possuem todas as permissões dos pacientes e médicos.

- **Cadastrar novos médicos:** Os administradores têm a capacidade de cadastrar novos médicos na plataforma, adicionando-os ao sistema.

- **Deletar contas dos médicos:** Os administradores podem excluir as contas de médicos da aplicação, se necessário, gerenciando o acesso dos médicos à plataforma.

Essas permissões são atribuídas de forma apropriada a cada tipo de usuário dentro da aplicação, garantindo que cada entidade possa realizar apenas as operações relevantes ao seu papel no sistema.

## Tecnologias Utilizadas

- Spring Boot 3.2.4
- Spring Data JPA 3.2.4
- Spring Data Redis 3.2.4
- Spring Boot Security 3.2.4
- Java 21
- Docker 26.0.2
- Docker Compose 2.26.1
- Maven 3.9.6
- PostgreSQL 16.2 - Alpine 3.19
- Redis 7.2.4 - Alpine
- Open Feign 4.1.1
- OpenAPI 2.0.3
- Jsonwebtoken 0.12.5
- Lombok 1.18.32
- Jedis 5.1.2

## Instalação e Execução Local

1. Clone o repositório para sua máquina local:

```
git clone git@github.com:italomaia03/agendamento-consultas.git
```

2. Navegue até o diretório do projeto:

```
cd nome_do_diretorio
```

3. Para testar sua aplicação, se for a primeira vez que você está abrindo-a, adicione o seguinte comando:

```
mvn wrapper:wrapper
``` 

OBS.: O Maven deve estar instalado e dentro das variáveis de ambiente do seu SO para o comando mvn funcionar.

4. Certifique-se de ter o Docker e Docker Compose instalados em sua máquina.

5. Execute o comando Docker Compose para iniciar os contêineres do PostgreSQL e Redis:

```
docker-compose up -d --build
```

6. Aguarde até que os contêineres estejam em execução.

8. A aplicação estará disponível em `http://localhost:8080`.

9. A documentação estará disponível em `http://localhost:8080/swagger-ui/index.html#/`.

## Principais Aprendizados

Durante o desenvolvimento desta aplicação de agendamento de consultas médicas, adquirimos diversos aprendizados fundamentais. Primeiramente, compreendemos os princípios básicos da arquitetura REST, incluindo a definição de recursos, URIs e o uso adequado dos verbos HTTP para manipulação desses recursos. Em seguida, aprofundamos nosso conhecimento no framework Spring Boot, utilizando-o para agilizar o desenvolvimento de aplicativos Java, aproveitando sua configuração automática e convenções sobre configuração. Ao implementarmos a segurança na aplicação com o Spring Security, aprendemos a proteger os recursos da aplicação e controlar o acesso por meio de autenticação e autorização, garantindo a segurança dos dados sensíveis. A integração de tokens JWT para autenticação stateless proporcionou-nos uma compreensão mais profunda sobre autenticação baseada em token e como ela pode ser aplicada de forma eficaz em sistemas distribuídos. Por fim, a combinação desses aprendizados permitiu não apenas o desenvolvimento de uma aplicação funcional, mas também a compreensão dos aspectos essenciais de segurança e arquitetura em aplicações RESTful.