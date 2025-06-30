package DAO;

import Principal.ConexaoBD;
import Principal.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Paciente.
 * Gerencia todas as operações de banco de dados para os pacientes.
 */
public class PacienteDAO {

    /**
     * Insere um novo paciente no banco de dados.
     *
     * @param paciente O objeto Paciente a ser inserido.
     */
    public void inserir(Paciente paciente) {
        String sql = "INSERT INTO pacientes (nome, cpf, telefone) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setString(3, paciente.getTelefone());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    paciente.setId(rs.getInt(1));
                    System.out.println("Paciente inserido com sucesso! ID: " + paciente.getId());
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                System.err.println("Erro ao inserir paciente: O CPF '" + paciente.getCpf() + "' já está cadastrado.");
            } else {
                System.err.println("Erro ao inserir paciente: " + e.getMessage());
            }
        }
    }

    /**
     * Busca todos os pacientes cadastrados no banco de dados.
     *
     * @return Uma lista de objetos Paciente.
     */
    public List<Paciente> buscarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";

        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paciente paciente = new Paciente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone")
                );
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pacientes: " + e.getMessage());
        }
        return pacientes;
    }
    
    /**
     * Atualiza os dados de um paciente de forma dinâmica.
     * Apenas os campos não nulos no objeto de entrada serão atualizados.
     *
     * @param paciente O objeto Paciente contendo o ID e os campos a serem alterados.
     */
    public void atualizar(Paciente paciente) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE pacientes SET ");
        List<Object> params = new ArrayList<>();

        if (paciente.getNome() != null && !paciente.getNome().isEmpty()) {
            sqlBuilder.append("nome = ?, ");
            params.add(paciente.getNome());
        }
        if (paciente.getCpf() != null && !paciente.getCpf().isEmpty()) {
            sqlBuilder.append("cpf = ?, ");
            params.add(paciente.getCpf());
        }
        if (paciente.getTelefone() != null && !paciente.getTelefone().isEmpty()) {
            sqlBuilder.append("telefone = ?, ");
            params.add(paciente.getTelefone());
        }
        
        if (params.isEmpty()) {
            System.out.println("Nenhum campo fornecido para atualização do paciente.");
            return;
        }

        sqlBuilder.setLength(sqlBuilder.length() - 2); 
        sqlBuilder.append(" WHERE id = ?");
        params.add(paciente.getId());

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Paciente atualizado com sucesso!");
            } else {
                System.out.println("Nenhum paciente encontrado com o ID fornecido.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    /**
     * Deleta um paciente do banco de dados com base no seu ID.
     *
     * @param id O ID do paciente a ser deletado.
     */
    public void deletar(int id) {
     
        String sql = "DELETE FROM pacientes WHERE id = ?";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Paciente deletado com sucesso!");
            } else {
                System.out.println("Nenhum paciente encontrado com o ID fornecido para deleção.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar paciente: " + e.getMessage());
        }
    }
}