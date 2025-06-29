package Principal;

import DAO.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PacienteDAO pacienteDAO = new PacienteDAO();
        HistoricoDAO historicoDAO = new HistoricoDAO();

        // --- ETAPA 1: CRIANDO UM NOVO PACIENTE E SEU HISTÓRICO ---
        System.out.println("--- Criando novo paciente ---");
        
        // 1. Crie e salve o paciente PRIMEIRO para obter o ID
        Paciente novoPaciente = new Paciente("Joana Silva", "987.654.321-00", "(81) 91122-3344");
        pacienteDAO.inserir(novoPaciente); // Agora, novoPaciente.getId() tem um valor!

        // 2. Crie e salve o histórico DEPOIS, usando o ID do paciente
        Historico historicoDaJoana = new Historico(novoPaciente.getId(), "Paciente chegou com dor no siso.");
        historicoDAO.inserir(historicoDaJoana); // Agora, historicoDaJoana.getId() tem um valor!

        // 3. Associe o histórico ao objeto do paciente em memória
        novoPaciente.setHistorico(historicoDaJoana);


        // --- ETAPA 2: CARREGANDO E MODIFICANDO O PACIENTE ---
        System.out.println("--- Carregando e modificando o paciente ---");
        
        // Suponha que você fechou e abriu o programa. Vamos carregar a Joana do banco.
        // Em um sistema real, você buscaria por CPF ou nome. Aqui, usamos o ID que já temos.
        List<Paciente> pacientes = pacienteDAO.buscarTodos();
        Paciente pacienteCarregado = pacientes.get(pacientes.size() - 1); // Pegando o último inserido

        // Carregue o histórico dele
        Historico historicoCarregado = historicoDAO.buscarPorPacienteId(pacienteCarregado.getId());
        
        // Associe o histórico carregado
        pacienteCarregado.setHistorico(historicoCarregado);
        
        System.out.println("Descrição atual: " + pacienteCarregado.getHistorico().getDescricao());

        // 4. Use os novos métodos para modificar o histórico de forma fácil!
        System.out.println("Atualizando descrição e adicionando imagem...");
        pacienteCarregado.definirDescricaoHistorico("Siso extraído com sucesso. Paciente liberada.", historicoDAO);
        pacienteCarregado.adicionarImagemAoHistorico("imagens_historico/raio_x_joana.jpg", historicoDAO);

        // Verificando a mudança
        Historico historicoAtualizado = historicoDAO.buscarPorPacienteId(pacienteCarregado.getId());
        System.out.println("Nova descrição: " + historicoAtualizado.getDescricao());
        System.out.println("Imagens salvas: " + historicoAtualizado.getCaminhosImagens());
    }
}