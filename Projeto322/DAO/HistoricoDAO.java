package DAO;

import Principal.ConexaoBD;
import Principal.Historico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Historico.
 * Gerencia todas as operações de banco de dados para históricos e suas imagens associadas.
 */
public class HistoricoDAO {

    /**
     * Insere um novo registro de histórico no banco de dados.
     * Após a inserção, atualiza o ID do objeto Historico com o ID gerado pelo banco.
     *
     * historico O objeto Historico a ser inserido.
     * return true se a inserção for bem-sucedida, false caso contrário.
     */
    public boolean inserir(Historico historico) {
        // A coluna 'descricao' corresponde ao campo 'descricao'
        String sql = "INSERT INTO historicos (paciente_id, descricao) VALUES (?, ?)";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, historico.getPacienteId());
            stmt.setString(2, historico.getDescricao());
            stmt.executeUpdate();

            // Recupera o ID gerado para o histórico e o define no objeto
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    historico.setId(rs.getInt(1));
                }
            }
            System.out.println("Histórico inserido com sucesso! ID: " + historico.getId());
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir histórico: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca um histórico com base no ID do paciente.
     * Carrega a descrição e todos os caminhos de imagem associados.
     *
     * pacienteId O ID do paciente.
     * return Um objeto Historico preenchido, ou null se não for encontrado.
     */
    public Historico buscarPorPacienteId(int pacienteId) {
        Historico historico = null;
        String sql = "SELECT * FROM historicos WHERE paciente_id = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pacienteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                historico = new Historico();
                historico.setId(rs.getInt("id"));
                historico.setPacienteId(rs.getInt("paciente_id"));
                historico.setDescricao(rs.getString("descricao"));
                
                // Após encontrar o histórico, busca as imagens associadas
                historico.setCaminhosImagens(buscarImagens(historico.getId()));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico por paciente: " + e.getMessage());
        }
        return historico;
    }

    /**
     * Atualiza a descrição de um histórico existente.
     *
     * historico O objeto Historico com o ID e a nova descrição.
     */
    public void atualizar(Historico historico) {
        String sql = "UPDATE historicos SET descricao = ? WHERE id = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, historico.getDescricao());
            stmt.setInt(2, historico.getId());
            stmt.executeUpdate();
            System.out.println("Descrição do histórico atualizada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar histórico: " + e.getMessage());
        }
    }
    
    /**
     * Adiciona o caminho de uma nova imagem associada a um histórico.
     *
     * historicoId O ID do histórico ao qual a imagem pertence.
     * caminhoImagem O caminho relativo da imagem a ser salvo.
     */
    public void adicionarImagem(int historicoId, String caminhoImagem) {
        String sql = "INSERT INTO historico_imagens (historico_id, caminho_imagem) VALUES (?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, historicoId);
            stmt.setString(2, caminhoImagem);
            stmt.executeUpdate();
            System.out.println("Caminho da imagem inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir caminho da imagem: " + e.getMessage());
        }
    }

    /**
     * Busca todos os caminhos de imagens de um histórico específico.
     *
     * historicoId O ID do histórico.
     * return Uma lista com os caminhos das imagens.
     */
    public List<String> buscarImagens(int historicoId) {
        List<String> caminhos = new ArrayList<>();
        String sql = "SELECT caminho_imagem FROM historico_imagens WHERE historico_id = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, historicoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                caminhos.add(rs.getString("caminho_imagem"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar caminhos de imagens: " + e.getMessage());
        }
        return caminhos;
    }
}