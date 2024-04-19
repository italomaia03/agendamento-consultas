# agendamento-consultas

## Fluxo de trabalho do Git (terminal)
 
## Criar branch pessoal
git checkout -b seu_nome

## Verificar a branch ativa
git branch

Ex.: 
main
* seu_nome -> esta é a branch que está ativa no momento

## Mudar para a main e atualizar
- Garanta que na sua branch atual não há novos commits a serem feitos e faça os seguintes comandos:

git checkout main
git pull origin main
git checkout seu_nome
git merge main
  
- A partir de agora sua branch de trabalho está atualizada e pronta para realizar novas alterações no código

## Para testar sua aplicação

Se for a primeira vez que você está abrindo adicione o seguinte comando:

mvn wrapper:wrapper

Ps. O maven deve está instalado e dentro das variáveis de ambiente para o comando mvn funcionar.

Após esse comando, na raiz do projeto, onde se localiza o arquivo docker-compose.yml:

docker compose up

Ao realizar alterações novas alterações no código, acabe com a execução do container e digite:

docker compose build
