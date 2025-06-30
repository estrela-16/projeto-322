package DAO;

import Principal.ConexaoBD;
import Principal.Materiais;
import Principal.Procedimento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedimentoDAO {

    /**
     * Insere um novo procedimento e seus materiais associados.
     * A operação é feita em uma transação para garantir a consistência.
     */
    public void inserir(Procedimento procedimento) {
        String sqlProcedimento = "INSERT INTO procedimentos (nome, especialidade) VALUES (?, ?)";
        String sqlMateriais = "INSERT INTO procedimento_materiais (procedimento_id, material_id) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Insere o procedimento principal
            try (PreparedStatement stmt = conn.prepareStatement(sqlProcedimento, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, procedimento.getNome());
                stmt.setString(2, procedimento.getEspecialidade());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        procedimento.setId(rs.getInt(1));
                    }
                }
            }

            // 2. Insere os materiais na tabela de junção
            try (PreparedStatement stmt = conn.prepareStatement(sqlMateriais)) {
                for (Materiais material : procedimento.getMateriais()) {
                    stmt.setInt(1, procedimento.getId());
                    stmt.setInt(2, material.getId());
                    stmt.addBatch(); // Adiciona a operação ao lote
                }
                stmt.executeBatch(); // Executa todas as inserções de uma vez
            }

            conn.commit(); // Confirma a transação
            System.out.println("Procedimento inserido com sucesso! ID: " + procedimento.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao inserir procedimento: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter transação: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Busca todos os procedimentos e carrega seus materiais associados.
     */
    public List<Procedimento> buscarTodos() {
        List<Procedimento> procedimentos = new ArrayList<>();
        String sql = "SELECT * FROM procedimentos";

        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String especialidade = rs.getString("especialidade");
                
                // Construtor foi ajustado para não precisar do preço
                Procedimento procedimento = new Procedimento(id, nome, especialidade);
                
                // Carrega os materiais para este procedimento
                procedimento.setMateriais(buscarMateriaisPorProcedimentoId(id));
                
                procedimentos.add(procedimento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar procedimentos: " + e.getMessage());
        }
        return procedimentos;
    }

    /**
     * Método auxiliar para buscar os materiais de um procedimento específico.
     */
    private List<Materiais> buscarMateriaisPorProcedimentoId(int procedimentoId) {
        List<Materiais> materiais = new ArrayList<>();
        // Query que junta as tabelas para encontrar os materiais
        String sql = "SELECT m.id, m.nome, m.valor FROM materiais m " +
                     "JOIN procedimento_materiais pm ON m.id = pm.material_id " +
                     "WHERE pm.procedimento_id = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, procedimentoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                materiais.add(new Materiais(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("valor")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar materiais do procedimento: " + e.getMessage());
        }
        return materiais;
    }
    /**
     * Atualiza os dados de um procedimento de forma dinâmica.
     */
    public void atualizar(Procedimento procedimento) {
    // SQL para atualizar a tabela principal de procedimentos
    String sqlUpdateProcedimento = "UPDATE procedimentos SET nome = ?, especialidade = ?, preco = ? WHERE id = ?";
    
    // SQL para apagar as associações de materiais antigas
    String sqlDeleteMateriais = "DELETE FROM procedimento_materiais WHERE procedimento_id = ?";

    // SQL para inserir as novas associações de materiais
    String sqlInsertMateriais = "INSERT INTO procedimento_materiais (procedimento_id, material_id) VALUES (?, ?)";

    Connection conn = null;
    try {
        conn = ConexaoBD.conectar();
        // Desativa o auto-commit para controlar a transação manualmente
        conn.setAutoCommit(false);

        // 1. Atualiza os dados na tabela 'procedimentos'
        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateProcedimento)) {
            stmt.setString(1, procedimento.getNome());
            stmt.setString(2, procedimento.getEspecialidade());
            stmt.setDouble(3, procedimento.getPreco());
            stmt.setInt(4, procedimento.getId());
            stmt.executeUpdate();
        }

        // 2. Deleta as associações de materiais antigas
        try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteMateriais)) {
            stmt.setInt(1, procedimento.getId());
            stmt.executeUpdate();
        }

        // 3. Insere as novas associações de materiais
        try (PreparedStatement stmt = conn.prepareStatement(sqlInsertMateriais)) {
            for (Materiais material : procedimento.getMateriais()) {
                stmt.setInt(1, procedimento.getId());
                stmt.setInt(2, material.getId());
                stmt.addBatch(); // Adiciona a operação em lote
            }
            stmt.executeBatch(); // Executa o lote de inserções
        }

        // 4. Se tudo ocorreu sem erros, confirma a transação
        conn.commit();
        System.out.println("Procedimento atualizado com sucesso!");

    } catch (SQLException e) {
        System.err.println("Erro ao atualizar procedimento, revertendo a transação: " + e.getMessage());
        // 5. Em caso de qualquer erro, reverte todas as alterações feitas
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro crítico ao tentar reverter a transação: " + ex.getMessage());
            }
        }
    } finally {
        // Garante que a conexão seja fechada e o auto-commit reativado
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}


    /**
     * Deleta um procedimento do banco de dados com base no seu ID.
     */
    public void deletar(int id) {
        String sql = "DELETE FROM procedimentos WHERE id = ?";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Procedimento deletado com sucesso!");
            } else {
                System.out.println("Nenhum procedimento encontrado com o ID fornecido para deleção.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar procedimento: " + e.getMessage());
        }
    }
}
