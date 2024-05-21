Feature: Paciente
    Scenario: Cadastrar com Sucesso
        Given paciente válido e não cadastrado
        When cadastrar o paciente
        Then paciente consegue logar na plataforma
    Scenario: O Administrador do sistema deve conseguir localizar o paciente cadastrado
        Given email de um paciente válido
        When o administrador estiver logado
        Then deve retorna o paciente com o email
    Scenario: Cadastrar paciente com Cep não rastreado
        Given paciente com um cep não rastreado
        When Cadastrar paciente com cep não rastreado
        Then Deve retornar um erro 404
