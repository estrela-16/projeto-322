package Principal;

import DAO.HistoricoDAO;
import DAO.PacienteDAO;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CriadorTabelas.criarTabelas();

        PacienteDAO pacienteDAO = new PacienteDAO();
        HistoricoDAO historicoDAO = new HistoricoDAO();

        // --- ETAPA 1: CRIANDO UM NOVO PACIENTE E SEU HISTÓRICO ---
        System.out.println("--- Criando novo paciente ---");
        
        Paciente novoPaciente = new Paciente("Joana Silva", "987.654.321-00", "(81) 91122-3344");
        pacienteDAO.inserir(novoPaciente); 

        Historico historicoDaJoana = new Historico(novoPaciente.getId(), "Paciente chegou com dor no siso.");
        historicoDAO.inserir(historicoDaJoana);

        novoPaciente.setHistorico(historicoDaJoana);
        System.out.println("Paciente e histórico inicial criados com sucesso.");

        System.out.println("\n--- Listando pacientes e seus históricos ---");
        
        List<Paciente> pacientes = pacienteDAO.buscarTodos();
        for (Paciente p : pacientes) {
            // ==================================================================
            // ===== CORREÇÃO PRINCIPAL: BUSCANDO O HISTÓRICO PARA CADA PACIENTE =====
            // ==================================================================
            // 1. Para cada paciente 'p', usamos o ID dele para buscar seu histórico.
            Historico h = historicoDAO.buscarPorPacienteId(p.getId());
            
            // 2. Associamos o histórico encontrado ao objeto do paciente.
            p.setHistorico(h); 
            
            // 3. Verificamos se o histórico não é nulo antes de usar.
            String descricaoHistorico = (p.getHistorico() != null) 
                                        ? p.getHistorico().getDescricao() 
                                        : "Nenhum histórico encontrado.";

            System.out.println(
                "ID: " + p.getId() + 
                " | Nome: " + p.getNome() + 
                " | Telefone: " + p.getTelefone() + 
                " | historico: " + descricaoHistorico
            );
        }
        System.out.println("\n");


        // --- ETAPA 2: CARREGANDO E MODIFICANDO O PACIENTE ---
        System.out.println("--- Carregando e modificando o paciente ---");
        
        // Esta parte já estava correta, pois buscava o histórico antes de usar.
        Paciente pacienteCarregado = pacientes.get(pacientes.size() - 1); 
        
        System.out.println("Descrição atual: " + pacienteCarregado.getHistorico().getDescricao());

        System.out.println("Atualizando descrição e adicionando imagem...");
        pacienteCarregado.definirDescricaoHistorico("Siso extraído com sucesso. Paciente liberada.", historicoDAO);
        pacienteCarregado.adicionarImagemAoHistorico("imagens_historico/raio_x_joana.jpg", historicoDAO);

        Historico historicoAtualizado = historicoDAO.buscarPorPacienteId(pacienteCarregado.getId());
        System.out.println("Nova descrição: " + historicoAtualizado.getDescricao());
        System.out.println("Imagens salvas: " + historicoAtualizado.getCaminhosImagens());

    }
}
