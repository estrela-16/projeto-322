

package DAO;

import Principal.ConexaoBD;
import Principal.Contas;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContasDAO { // banco de dados com a conta

    public void inserir(Contas conta) {
        String sql = "INSERT INTO contas (nome, valor) VALUES (?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, conta.getNome());
            stmt.setDouble(2, conta.getValor());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    conta.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir conta: " + e.getMessage());
        }
    }

    public List<Contas> buscarTodos() {
        List<Contas> lista = new ArrayList<>();
        String sql = "SELECT * FROM contas";
        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Contas(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("valor")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar contas: " + e.getMessage());
        }
        return lista;
    }

    public void deletar(int id) {
        String sql = "DELETE FROM contas WHERE id = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar conta: " + e.getMessage());
        }
    }
}