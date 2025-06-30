package DAO;

import Principal.ConexaoBD;
import Principal.Materiais;
import Principal.Procedimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProcedimentoDAO {

    /**
     * Insere um novo procedimento no banco de dados.
     */
    public void inserir(Procedimento procedimento) {
        // CORREÇÃO: Adicionada a coluna 'especialidade'
        String sqlProcedimento = "INSERT INTO procedimentos (nome, especialidade, preco) VALUES (?, ?, ?)";
        String sqlMateriais = "INSERT INTO procedimento_materiais (procedimento_id, material_id) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement stmtProcedimento = null;
        PreparedStatement stmtMateriais = null;

        
        try {
            conn = ConexaoBD.conectar();
            // Desativa o auto-commit para garantir que a operação seja atômica (tudo ou nada)
            conn.setAutoCommit(false);

            // Inserir o procedimento e obter o ID gerado
            stmtProcedimento = conn.prepareStatement(sqlProcedimento, Statement.RETURN_GENERATED_KEYS);
            stmtProcedimento.setString(1, procedimento.getNome());
            stmtProcedimento.setString(2, procedimento.getEspecialidade());
            stmtProcedimento.setDouble(3, procedimento.getPreco());
            stmtProcedimento.executeUpdate();

            try (ResultSet rs = stmtProcedimento.getGeneratedKeys()) {
                if (rs.next()) {
                    procedimento.setId(rs.getInt(1));
                }
            }

            // Inserir os materiais associados na tabela de junção
            stmtMateriais = conn.prepareStatement(sqlMateriais);
            for (Materiais material : procedimento.getMateriais()) {
                stmtMateriais.setInt(1, procedimento.getId());
                stmtMateriais.setInt(2, material.getId());
                stmtMateriais.addBatch(); // Adiciona a operação em um lote para execução em massa
            }
            stmtMateriais.executeBatch(); // Executa todas as inserções de materiais

            // Se tudo correu bem, confirma as alterações no banco
            conn.commit();
            System.out.println("Procedimento e materiais inseridos com sucesso! ID: " + procedimento.getId());

        } catch (SQLException e) {
            // Em caso de erro, desfaz qualquer alteração
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter transação: " + ex.getMessage());
                }
            }
            if (e.getErrorCode() == 19) { // UNIQUE constraint failed
                System.err.println("Erro ao inserir procedimento: O nome '" + procedimento.getNome() + "' já existe.");
            } else {
                System.err.println("Erro ao inserir procedimento: " + e.getMessage());
            }
        } finally {
            // Fecha as conexões e reativa o auto-commit
            try {
                if (stmtProcedimento != null) stmtProcedimento.close();
                if (stmtMateriais != null) stmtMateriais.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    /**
     * Busca todos os procedimentos cadastrados no banco de dados.
     */
    public List<Procedimento> buscarTodos() {
        List<Procedimento> procedimentos = new ArrayList<>();
        String sql = "SELECT * FROM procedimentos";

        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Procedimento procedimento = new Procedimento(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("especialidade"),
                    rs.getDouble("preco") // CORREÇÃO: Nome da coluna sem cedilha
                );
                procedimento.getMateriais().addAll(buscarMateriaisPorProcedimentoId(procedimento.getId(), conn));
                procedimentos.add(procedimento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar procedimentos: " + e.getMessage());
        }
        return procedimentos;
    }

    private List<Materiais> buscarMateriaisPorProcedimentoId(int procedimentoId, Connection conn) throws SQLException {
        List<Materiais> materiais = new ArrayList<>();
        String sql = "SELECT m.id, m.nome, m.valor " +
                     "FROM materiais m " +
                     "JOIN procedimento_materiais pm ON m.id = pm.material_id " +
                     "WHERE pm.procedimento_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, procedimentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    materiais.add(new Materiais(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("valor")
                    ));
                }
            }
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
