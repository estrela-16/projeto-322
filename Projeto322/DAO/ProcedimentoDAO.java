package DAO;

import Principal.ConexaoBD;
import Principal.Procedimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Procedimento.
 * Gerencia as operações de banco de dados para os procedimentos oferecidos.
 * Nota: Este DAO gerencia apenas a tabela 'procedimentos'. A associação com
 * 'materiais' requer uma lógica de tabela de junção separada.
 */
public class ProcedimentoDAO {

    /**
     * Insere um novo procedimento no banco de dados.
     *
     * @param procedimento O objeto Procedimento a ser inserido.
     */
    public void inserir(Procedimento procedimento) {
        // A tabela não tem 'especialidade', então não a incluímos no SQL.
        String sql = "INSERT INTO procedimentos (nome, preco) VALUES (?, ?)";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, procedimento.getNome());
            stmt.setDouble(2, procedimento.getPreco());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    procedimento.setId(rs.getInt(1));
                    System.out.println("Procedimento inserido com sucesso! ID: " + procedimento.getId());
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 19) { // UNIQUE constraint failed
                System.err.println("Erro ao inserir procedimento: O nome '" + procedimento.getNome() + "' já existe.");
            } else {
                System.err.println("Erro ao inserir procedimento: " + e.getMessage());
            }
        }
    }

    /**
     * Busca todos os procedimentos cadastrados no banco de dados.
     *
     * @return Uma lista de objetos Procedimento.
     */
    public List<Procedimento> buscarTodos() {
        List<Procedimento> procedimentos = new ArrayList<>();
        String sql = "SELECT * FROM procedimentos";

        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Criamos o objeto Procedimento. A especialidade não vem do banco, fica como null.
                Procedimento procedimento = new Procedimento(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("especialidade"),
                    rs.getDouble("preço")
                );
                procedimentos.add(procedimento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar procedimentos: " + e.getMessage());
        }
        return procedimentos;
    }
    
    /**
     * Atualiza os dados de um procedimento de forma dinâmica.
     *
     * @param procedimento O objeto Procedimento contendo o ID e os campos a serem alterados.
     */
    public void atualizar(Procedimento procedimento) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE procedimentos SET ");
        List<Object> params = new ArrayList<>();

        if (procedimento.getNome() != null && !procedimento.getNome().isEmpty()) {
            sqlBuilder.append("nome = ?, ");
            params.add(procedimento.getNome());
        }
        if (procedimento.getPreco() != 0) {
            sqlBuilder.append("preco = ?, ");
            params.add(procedimento.getPreco());
        }
        
        if (params.isEmpty()) {
            System.out.println("Nenhum campo fornecido para atualização do procedimento.");
            return;
        }

        sqlBuilder.setLength(sqlBuilder.length() - 2);
        sqlBuilder.append(" WHERE id = ?");
        params.add(procedimento.getId());

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Procedimento atualizado com sucesso!");
            } else {
                System.out.println("Nenhum procedimento encontrado com o ID fornecido.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar procedimento: " + e.getMessage());
        }
    }

    /**
     * Deleta um procedimento do banco de dados com base no seu ID.
     *
     * @param id O ID do procedimento a ser deletado.
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
