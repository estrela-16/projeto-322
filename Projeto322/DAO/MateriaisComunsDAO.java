package DAO;

import Principal.ConexaoBD;
import Principal.MateriaisComuns;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade MateriaisComuns.
 * Gerencia as operações de banco de dados para os materiais de uso geral do consultório.
 */
public class MateriaisComunsDAO {

    /**
     * Insere um novo material comum no banco de dados.
     *
     * @param material O objeto MateriaisComuns a ser inserido.
     */
    public void inserir(MateriaisComuns material) {
        String sql = "INSERT INTO materiais_comuns (nome, valor, quantidade) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, material.getNome());
            stmt.setDouble(2, material.getValor());
            stmt.setInt(3, material.getQuantidade());
            stmt.executeUpdate();

            // Recupera o ID gerado e o define no objeto
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    material.setId(rs.getInt(1));
                    System.out.println("Material comum inserido com sucesso! ID: " + material.getId());
                }
            }
        } catch (SQLException e) {
            // Trata o erro de violação de chave única (nome duplicado)
            if (e.getErrorCode() == 19) { // Código de erro do SQLite para 'UNIQUE constraint failed'
                System.err.println("Erro ao inserir material comum: O nome '" + material.getNome() + "' já existe.");
            } else {
                System.err.println("Erro ao inserir material comum: " + e.getMessage());
            }
        }
    }

    /**
     * Busca todos os materiais comuns cadastrados no banco de dados.
     *
     * @return Uma lista de objetos MateriaisComuns.
     */
    public List<MateriaisComuns> buscarTodos() {
        List<MateriaisComuns> lista = new ArrayList<>();
        String sql = "SELECT * FROM materiais_comuns";

        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MateriaisComuns material = new MateriaisComuns(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("valor"),
                    rs.getInt("quantidade")
                );
                lista.add(material);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar materiais comuns: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Deleta um material comum do banco de dados com base no seu ID.
     *
     * @param id O ID do material a ser deletado.
     */
    public void deletar(int id) {
        String sql = "DELETE FROM materiais_comuns WHERE id = ?";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Material comum deletado com sucesso!");
            } else {
                System.out.println("Nenhum material comum encontrado com o ID fornecido para deleção.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar material comum: " + e.getMessage());
        }
    }
}