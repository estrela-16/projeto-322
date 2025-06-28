
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Importe sua classe Dentista aqui (assumindo que você a tenha)
// Exemplo: import seu_pacote.Dentista;

public class DentistaDAO {

    public void inserir(Dentista dentista) {
        String sql = "INSERT INTO dentistas (nome, cpf, cro, dados_bancarios) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getCpf());
            stmt.setString(3, dentista.getCro());
            stmt.setString(4, dentista.getDadosBancarios());
            stmt.executeUpdate();
            System.out.println("Dentista inserido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir dentista: " + e.getMessage());
        }
    }

    public List<Dentista> buscarTodos() {
        List<Dentista> dentistas = new ArrayList<>();
        String sql = "SELECT * FROM dentistas";
        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Dentista dentista = new Dentista();
                dentista.setId(rs.getInt("id")); // Adicione um setId na sua classe Dentista
                dentista.setNome(rs.getString("nome"));
                dentista.setCpf(rs.getString("cpf"));
                dentista.setCro(rs.getString("cro"));
                dentista.setDadosBancarios(rs.getString("dados_bancarios"));
                dentistas.add(dentista);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar dentistas: " + e.getMessage());
        }
        return dentistas;
    }

    // Métodos para atualizar e deletar podem ser adicionados aqui
    public void atualizar(Dentista dentista) {
        String sql = "UPDATE dentistas SET nome = ?, cpf = ?, cro = ?, dados_bancarios = ? WHERE id = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getCpf());
            stmt.setString(3, dentista.getCro());
            stmt.setString(4, dentista.getDadosBancarios());
            stmt.setInt(5, dentista.getId());
            stmt.executeUpdate();
            System.out.println("Dentista atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar dentista: " + e.getMessage());
        }
    }

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