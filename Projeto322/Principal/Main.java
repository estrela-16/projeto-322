package Principal;

import DAO.DentistaDAO;
import java.util.List;
// testar base de dados, nap usada para rodar o codigo n versao final
public class Main {

    public static void main(String[] args) {
        
        //  CRIAÇÃO DO BANCO DE DADOS E TABELAS
        // Isso garantirá que o arquivo .db e as tabelas existam antes de qualquer operação.
      
        System.out.println("--- 1. Inicializando o Banco de Dados ---");
        CriadorTabelas.criarTabelas();
        System.out.println("-----------------------------------------\n");

        System.out.println("--- 2. Inserindo novos dentistas ---");
        DentistaDAO dentistaDAO = new DentistaDAO();

        Dentista drCarlos = new Dentista("Dr. Carlos Andrade", "111.222.333-44", "(11) 91234-5678", "CRO-SP-12345");
        dentistaDAO.inserir(drCarlos); 
     
        System.out.println("ID do Dr. Carlos no banco: " + drCarlos.getId());

        Dentista draAna = new Dentista("Dra. Ana Souza", "555.666.777-88", "(21) 98765-4321", "CRO-RJ-67890");
        dentistaDAO.inserir(draAna);
        System.out.println("ID da Dra. Ana no banco: " + draAna.getId());
        System.out.println("--------------------------------------\n");

        System.out.println("--- 3. Listando todos os dentistas cadastrados ---");
        List<Dentista> todosOsDentistas = dentistaDAO.buscarTodos();
        for (Dentista d : todosOsDentistas) {
            System.out.println(
                "ID: " + d.getId() + 
                " | Nome: " + d.getNome() + 
                " | Telefone: " + d.getTelefone() + 
                " | CRO: " + d.getCro()
            );
        }
        System.out.println("---------------------------------------------------\n");


        System.out.println("--- 4. Atualizando o telefone do Dr. Carlos ---");
        
 
        Dentista atualizacaoCarlos = new Dentista();
        atualizacaoCarlos.setId(drCarlos.getId());
        atualizacaoCarlos.setTelefone("(11) 99999-8888");

        dentistaDAO.atualizar(atualizacaoCarlos);
        System.out.println("------------------------------------------------\n");

        System.out.println("--- 5. Deletando a Dra. Ana ---");
        dentistaDAO.deletar(draAna.getId());
        System.out.println("--------------------------------\n");


  
        System.out.println("--- 6. Lista final de dentistas ---");
        List<Dentista> dentistasFinais = dentistaDAO.buscarTodos();
        if (dentistasFinais.isEmpty()) {
            System.out.println("Nenhum dentista cadastrado no momento.");
        } else {
            for (Dentista d : dentistasFinais) {
                System.out.println(
                    "ID: " + d.getId() + 
                    " | Nome: " + d.getNome() + 
                    " | Telefone: " + d.getTelefone() + 
                    " | CRO: " + d.getCro()
                );
            }
        }
        System.out.println("-------------------------------------\n");
    }
}