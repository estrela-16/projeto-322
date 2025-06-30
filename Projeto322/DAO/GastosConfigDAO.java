package DAO;

import Principal.CalculodeGastos;
import Principal.ConexaoBD;
import java.sql.*;

/**
 * DAO (Data Access Object) para as configurações de gastos.
 * Gerencia a persistência dos parâmetros de cálculo (consultas, comissão, taxa).
 * Esta classe trabalha com uma única linha na tabela `gastos_config` (com id=1).
 */
public class GastosConfigDAO {

    private static final int CONFIG_ID = 1; // ID fixo para a linha de configuração

    /**
     * Salva ou atualiza as configurações de gastos no banco de dados.
    
     * @param config O objeto CalculodeGastos contendo os valores a serem salvos.
     */
    public void salvarOuAtualizar(CalculodeGastos config) {
        String sqlUpdate = "UPDATE gastos_config SET consultas_mes = ?, comissao = ?, taxa_servico = ? WHERE id = ?";
        String sqlInsert = "INSERT INTO gastos_config (id, consultas_mes, comissao, taxa_servico) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
           
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setInt(1, 1); 
                stmtUpdate.setDouble(2, config.getComissao());
                stmtUpdate.setDouble(3, config.getTaxaServico());
                stmtUpdate.setInt(4, CONFIG_ID);
                
                int affectedRows = stmtUpdate.executeUpdate();

                if (affectedRows == 0) {
                    try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                        stmtInsert.setInt(1, CONFIG_ID);
                        stmtInsert.setInt(2, 1);
                        stmtInsert.setDouble(3, config.getComissao());
                        stmtInsert.setDouble(4, config.getTaxaServico());
                        stmtInsert.executeUpdate();
                        System.out.println("Configurações de gastos inseridas.");
                    }
                } else {
                    System.out.println("Configurações de gastos atualizadas.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar ou atualizar configurações de gastos: " + e.getMessage());
        }
    }

    /**
     * Carrega as configurações de gastos do banco de dados.
     *
     * @return Um objeto CalculodeGastos preenchido com os dados do banco, ou um novo objeto com valores padrão se não houver dados.
     */
    public CalculodeGastos carregar() {
        String sql = "SELECT consultas_mes, comissao, taxa_servico FROM gastos_config WHERE id = ?";
        CalculodeGastos config = new CalculodeGastos(); // Cria um objeto com valores padrão

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, CONFIG_ID);
            ResultSet rs = stmt.executeQuery();

           
            if (rs.next()) {
                config.setNumeroDeConsultas(rs.getInt("consultas_mes"));
                config.setComissao(rs.getDouble("comissao"));
                config.setTaxaServico(rs.getDouble("taxa_servico"));
                System.out.println("Configurações de gastos carregadas do banco.");
            } else {
                 System.out.println("Nenhuma configuração de gastos encontrada no banco. Usando valores padrão.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar configurações de gastos: " + e.getMessage());
        }
        return config;
    }
}