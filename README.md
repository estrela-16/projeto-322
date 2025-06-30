# Sistema de Gerenciamento de Consultório Odontológico

## Visão Geral

Este é um sistema de desktop completo para gerenciamento de consultórios odontológicos, desenvolvido em Java com a biblioteca Swing para a interface gráfica. O sistema permite o cadastro e gerenciamento de pacientes, dentistas, materiais, procedimentos e agendamentos. Além disso, possui um módulo financeiro para acompanhamento de ganhos, despesas e balanço mensal.

A persistência dos dados é realizada através de um banco de dados **SQLite**, e a interação com o banco é gerenciada pelo padrão **DAO (Data Access Object)**.

## Pré-requisitos de Instalação

Para que o sistema funcione corretamente, é necessário ter o SQLite3 instalado na sua máquina. Se você estiver usando um sistema baseado em Debian/Ubuntu (como o Linux Mint), pode instalá-lo facilmente através do terminal.

Abra o terminal e execute os seguintes comandos:

```bash
sudo apt update
sudo apt-get install sqlite3
```

## Como o Sistema Funciona

### Conexão com o Banco de Dados: O Padrão DAO

Toda a comunicação entre a aplicação Java e o banco de dados SQLite é intermediada por uma camada de acesso a dados. Para cada entidade do sistema (como `Paciente`, `Dentista`, `Atendimento`, etc.), existe uma classe DAO correspondente (ex: `PacienteDAO`, `DentistaDAO`, `AtendimentoDAO`).

Essa abordagem organiza o código, separando as regras de negócio da lógica de persistência de dados. As classes DAO são responsáveis por realizar as operações de **CRUD (Create, Read, Update, Delete)** no banco de dados.

### Arquivos de Teste

Durante o desenvolvimento, foram utilizados arquivos específicos para testar funcionalidades isoladas:

  * `Main.java`: Este arquivo não faz parte da aplicação principal e foi usado exclusivamente para testar as operações do banco de dados, como inserção, busca e atualização de registros nas tabelas.
  * `TesteAdicionarImagemGUI.java`: Este arquivo foi criado para desenvolver e testar de forma isolada a funcionalidade de upload e visualização de imagens no histórico do paciente.

### Cálculo de Custos e Preços

O sistema possui uma forma dinâmica para calcular os custos envolvidos na operação do consultório e, consequentemente, o preço de cada procedimento.

#### 1\. Gastos Gerais

Na aba "Gastos Gerais" da interface, o usuário insere informações essenciais que servem de base para todos os outros cálculos:

  * **Contas:** Despesas fixas e variáveis do consultório (ex: aluguel, luz, salários).
  * **Materiais Básicos:** Materiais de consumo geral, não atrelados a um procedimento específico (ex: luvas, máscaras).
  * **Configurações de Cálculo:**
      * **Número médio de consultas por mês:** Este valor é usado para diluir o custo total das contas e materiais básicos entre os atendimentos.
      * **Comissão:** Percentual destinado ao pagamento dos dentistas.
      * **Taxa de Serviço:** Uma margem de lucro ou taxa administrativa aplicada sobre os custos.

O valor exibido como **"Gasto Total por consulta"** é calculado da seguinte forma (conforme a classe `CalculodeGastos`):

```
Gasto por Consulta = (Custo Total das Contas / N° de Consultas) + (Custo Total dos Materiais Básicos / N° de Consultas)
```

Este valor representa o custo operacional base para cada atendimento realizado no mês.

#### 2\. Preço do Procedimento

O preço sugerido para cada procedimento cadastrado é calculado automaticamente com base nos custos. A fórmula (presente na classe `Procedimento`) é:

```
Preço do Procedimento = ( (Soma dos Valores dos Materiais Específicos) + (Gasto por Consulta) ) * Taxa de Serviço
```

Ou seja, o preço final cobre o custo dos materiais usados especificamente naquele procedimento, mais uma fatia dos gastos gerais do consultório, e sobre esse total é aplicada a taxa de serviço (margem de lucro).

### Cálculos do Módulo Financeiro

A aba "Financeiro" apresenta um relatório anual, mês a mês. Os cálculos são realizados pela classe `Financeiro` da seguinte maneira:

  * **Ganhos (Bruto):** É a soma dos preços de todos os atendimentos realizados no mês.
  * **Gastos (Total):** É a soma de duas parcelas:
    1.  O **custo de todos os procedimentos** realizados no mês (soma dos materiais específicos + fatia dos gastos gerais).
    2.  A **comissão dos dentistas**, que é um percentual sobre o ganho bruto do mês.
  * **Lucro (Líquido):** É a diferença entre os Ganhos e os Gastos (`Lucro = Ganhos - Gastos`).

## Como Utilizar o Sistema: Ordem Recomendada

Para um funcionamento correto e para que os cálculos de preço sejam precisos, é altamente recomendado seguir a ordem abaixo ao inserir os dados no sistema pela primeira vez:

1.  **Cadastrar Dentistas e Pacientes:** Para rodar o código, execute o arquivo `TelaInicial.java`. Ele abrirá a primeira tela do sistema.
2.  **Cadastrar Dentistas e Pacientes:** Comece inserindo as pessoas nas abas "Dentistas" e "Pacientes".
3.  **Cadastrar Materiais Específicos:** Na aba "Materiais", adicione todos os materiais que são usados em procedimentos específicos (ex: resina, brocas, implantes).
4.  **Configurar Gastos Gerais:**
      * Vá para a aba **"Gastos Gerais"**.
      * Adicione todas as **Contas** fixas e variáveis (água, luz, aluguel, etc.).
      * Adicione os **Materiais Básicos** de uso comum. (Use ctrl para adicionar mais que um material para cada procedimento)
      * Preencha os campos de **Configurações de Cálculo** (nº médio de consultas, comissão e taxa de serviço) e clique em "Salvar".
5.  **Cadastrar Procedimentos:** Com todos os custos e materiais já inseridos, vá para a aba "Procedimentos". Ao criar um novo procedimento e associar os materiais a ele, o **preço será calculado automaticamente** com base nos dados que você inseriu no passo anterior.
6.  **Agendar Atendimentos:** Por fim, com tudo configurado, utilize a aba "Agenda" para marcar os atendimentos, associando um paciente, um dentista e um procedimento. O status do pagamento (pago/não pago) pode ser alterado diretamente na agenda.
