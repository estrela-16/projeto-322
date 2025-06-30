package DAO;

import Principal.ConexaoBD;
import Principal.Materiais;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Materiais.
 * Gerencia as operações de banco de dados para os materiais do consultório.
 */
public class MateriaisDAO {

    /**
     * Insere um novo material no banco de dados.
     * A coluna 'valor' no banco corresponde ao campo 'valor' na classe.
     *
     * @param material O objeto Materiais a ser inserido.
     */
    public void inserir(Materiais material) {
        String sql = "INSERT INTO materiais (nome, valor) VALUES (?, ?)";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, material.getNome());
            stmt.setDouble(2, material.getValor());
            stmt.executeUpdate();

            // Recupera o ID gerado pelo banco e o define no objeto
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1); // Pega o primeiro (e geralmente único) ID gerado
                    material.setId(idGerado);
                    System.out.println("Material inserido com sucesso! ID: " + material.getId());
                }
            }

        } catch (SQLException e) {
            // Trata o erro de violação de chave única (nome duplicado)
            if (e.getErrorCode() == 19) { // Código de erro do SQLite para 'UNIQUE constraint failed'
                System.err.println("Erro ao inserir material: O nome '" + material.getNome() + "' já existe.");
            } else {
                System.err.println("Erro ao inserir material: " + e.getMessage());
            }
        }
    }

    /**
     * Busca todos os materiais cadastrados no banco de dados.
     *
     * return Uma lista de objetos Materiais.
     */
    public List<Materiais> buscarTodos() {
        List<Materiais> materiais = new ArrayList<>();
        String sql = "SELECT * FROM materiais";

        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Materiais material = new Materiais(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("valor")
                );
                materiais.add(material);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar materiais: " + e.getMessage());
        }
        return materiais;
    }
    
    /**
     * Atualiza os dados de um material de forma dinâmica.
     * Apenas os campos não nulos (ou > 0 para valor) no objeto de entrada serão atualizados.
     *
     * param material O objeto Materiais contendo o ID e os campos a serem alterados.
     */
    public void atualizar(Materiais material) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE materiais SET ");
        List<Object> params = new ArrayList<>();

        // Verifica se o nome foi fornecido para atualização
        if (material.getNome() != null && !material.getNome().isEmpty()) {
            sqlBuilder.append("nome = ?, ");
            params.add(material.getNome());
        }

        // Verifica se o valor foi fornecido para atualização.
        // Assumimos que um valor 0 não é uma atualização intencional, mas o padrão.
        if (material.getValor() != 0) {
            sqlBuilder.append("valor = ?, ");
            params.add(material.getValor());
        }
        
        // Se a lista de parâmetros está vazia, nenhum campo foi alterado.
        if (params.isEmpty()) {
            System.out.println("Nenhum campo fornecido para atualização.");
            return;
        }

        // Remove a última vírgula e espaço ", "
        sqlBuilder.setLength(sqlBuilder.length() - 2);

        // Adiciona a cláusula WHERE para atualizar o material correto
        sqlBuilder.append(" WHERE id = ?");
        params.add(material.getId());

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            // Define os parâmetros na PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Material atualizado com sucesso!");
            } else {
                System.out.println("Nenhum material encontrado com o ID fornecido.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar material: " + e.getMessage());
        }
    }


    /**
     * Deleta um material do banco de dados com base no seu ID.
     *
     * param id O ID do material a ser deletado.
     */
    public void deletar(int id) {
        String sql = "DELETE FROM materiais WHERE id = ?";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Material deletado com sucesso!");
            } else {
                System.out.println("Nenhum material encontrado com o ID fornecido para deleção.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar material: " + e.getMessage());
        }
    }
}
