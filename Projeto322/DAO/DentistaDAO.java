package DAO;
import Principal.ConexaoBD;
import Principal.Dentista;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DentistaDAO {

    //insere dentista no BD
    public void inserir(Dentista dentista) {
        
        String sql = "INSERT INTO dentistas (nome, cpf, telefone, cro) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getCpf());
            stmt.setString(3, dentista.getTelefone());
            stmt.setString(4, dentista.getCro());
            stmt.executeUpdate(); // Executa a inserção

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) { 
                    int idGerado = rs.getInt(1); 
                    dentista.setId(idGerado); 
                    System.out.println("Dentista inserido com sucesso! ID gerado: " + idGerado);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir dentista: " + e.getMessage());
        }
    }


    //Faz uma lista com todos os dentistas armazenados na base de dados
    public List<Dentista> buscarTodos() {
        List<Dentista> dentistas = new ArrayList<>();
        String sql = "SELECT * FROM dentistas";
        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Dentista dentista = new Dentista(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("cro")
                );
                dentistas.add(dentista);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar dentistas: " + e.getMessage());
        }
        return dentistas;
    }
    public void atualizar(Dentista dentista) {
     
        StringBuilder sqlBuilder = new StringBuilder("UPDATE dentistas SET ");
        
        List<Object> params = new ArrayList<>();
        if (dentista.getNome() != null && !dentista.getNome().isEmpty()) {
            sqlBuilder.append("nome = ?, ");
            params.add(dentista.getNome());
        }
        if (dentista.getCpf() != null && !dentista.getCpf().isEmpty()) {
            sqlBuilder.append("cpf = ?, ");
            params.add(dentista.getCpf());
        }
        if (dentista.getTelefone() != null && !dentista.getTelefone().isEmpty()) {
            sqlBuilder.append("telefone = ?, ");
            params.add(dentista.getTelefone());
        }
        if (dentista.getCro() != null && !dentista.getCro().isEmpty()) {
            sqlBuilder.append("cro = ?, ");
            params.add(dentista.getCro());
        }

        if (params.isEmpty()) {
            System.out.println("Nenhum campo fornecido para atualização.");
            return;
        }

        sqlBuilder.setLength(sqlBuilder.length() - 2);

        sqlBuilder.append(" WHERE id = ?");
        params.add(dentista.getId());

        try (Connection conn = ConexaoBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
      
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Dentista atualizado com sucesso!");
            } else {
                System.out.println("Nenhum dentista encontrado com o ID fornecido.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar dentista: " + e.getMessage());
        }
    }

    //Deleta dentista da base de dados
    public void deletar(int id) {
        String sql = "DELETE FROM dentistas WHERE id = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Dentista deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar dentista: " + e.getMessage());
        }
    }
}