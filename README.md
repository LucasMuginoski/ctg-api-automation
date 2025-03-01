# ctg-api-automation

Repositório de estudos de automação de testes de API usando Gradle e restAssured.
<br>
Mentoria da CTG (Comunidade Tester Global), do Vinicius Pessoni.

A API usada para estudo é a API fornecida pelo próprio Vinni em suas aulas.
Disponível no github: https://github.com/vinnypessoni/api-clientes-exemplo-microservico

## O que aprendi:
Conhecimentos básicos de automação de testes de APIs
1. Como configurar ambiente para automatizar Testes de API com RestAssured, Java, JUnit5 e Gradle
2. Como criar casos de testes para métodos HTTP como GET,POST,PUT e DELETE

## Conceitos de framework de testes automatizados
Para que sejamos capazes de definir uma framework de testes automatizados, 
precisamos seguir os seguintes conceitos listados baixo
### Forma de executar os testes:
- **JUnit 5;**
### Froma de verificar os resultados:
- **JUnit 5/Hamcrest;**
### Linguagem de programação:
- **Java;**
### Ferramentas adicionais:
- **Simplificar requisições: restAssured;**
- **Gerenciar dependênciais e gerar relatórios: Gradle;**

## Boas práticas:
### Testes atômicos
Por default, o Junit executa os testes de uma forma **determinística** mas em uma ordem não previsível.
No Junit 5 podemos "forçar" uma ordem específica mas isso não é uma boa prática. Portanto, devemos evitar.

A boa prática dos testes atômicos se resume em cada teste ser auto contido, ou seja, ter tudo que é necessário para que ele seja executado.
Mínimo de dependências possíveis e **um teste não ser influenciado ou depender de outro**.

