package DAO;

import Principal.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AtendimentoDAO {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Insere um novo atendimento no banco de dados.
     * @param atendimento O objeto Atendimento a ser inserido.
     */
    public void inserir(Atendimento atendimento) {
        String sql = "INSERT INTO atendimentos (data, horario, status_pagamento, dentista_id, paciente_id, procedimento_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, atendimento.getData().format(DATE_FORMATTER));
            stmt.setString(2, atendimento.getHorario());
            stmt.setString(3, atendimento.getStatusP().name());
            stmt.setInt(4, atendimento.getDentista().getId());
            stmt.setInt(5, atendimento.getPaciente().getId());
            stmt.setInt(6, atendimento.getProcedimentos().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    atendimento.setId(rs.getInt(1));
                    System.out.println("Atendimento inserido com sucesso! ID: " + atendimento.getId());
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir atendimento: " + e.getMessage());
        }
    }

    /**
     * Busca todos os atendimentos, reconstruindo os objetos Dentista, Paciente e Procedimento associados.
     * @return Uma lista de objetos Atendimento.
     */
    public List<Atendimento> buscarTodos() {
        List<Atendimento> atendimentos = new ArrayList<>();
        // Query com JOIN para buscar todos os dados necessários de uma vez
        String sql = "SELECT " +
                 "a.id as atendimento_id, a.data, a.horario, a.status_pagamento, " +
                 "p.id as paciente_id, p.nome as paciente_nome, p.cpf as paciente_cpf, p.telefone as paciente_telefone, " +
                 "d.id as dentista_id, d.nome as dentista_nome, d.cpf as dentista_cpf, d.telefone as dentista_telefone, d.cro, " +
                 "proc.id as procedimento_id, proc.nome as procedimento_nome, proc.especialidade " +
                 "FROM atendimentos a " +
                 "JOIN pacientes p ON a.paciente_id = p.id " +
                 "JOIN dentistas d ON a.dentista_id = d.id " +
                 "JOIN procedimentos proc ON a.procedimento_id = proc.id";

        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Criar e preencher o objeto Dentista
                Dentista dentista = new Dentista(
                    rs.getInt("dentista_id"),
                    rs.getString("dentista_nome"),
                    rs.getString("dentista_cpf"),
                    rs.getString("dentista_telefone"),
                    rs.getString("cro")
                );

                // Criar e preencher o objeto Paciente
                Paciente paciente = new Paciente(
                    rs.getInt("paciente_id"),
                    rs.getString("paciente_nome"),
                    rs.getString("paciente_cpf"),
                    rs.getString("paciente_telefone")
                );

                // Criar e preencher o objeto Procedimento
                Procedimento procedimento = new Procedimento(
                    rs.getInt("procedimento_id"),
                    rs.getString("procedimento_nome"),
                    rs.getString("especialidade")
                );

                // Criar e preencher o objeto Atendimento
                Atendimento atendimento = new Atendimento();
                atendimento.setId(rs.getInt("atendimento_id"));
                atendimento.setData(LocalDate.parse(rs.getString("data"), DATE_FORMATTER));
                atendimento.setHorario(rs.getString("horario"));
                atendimento.setStatusP(StatusPagamento.valueOf(rs.getString("status_pagamento")));
                atendimento.setDentista(dentista);
                atendimento.setPaciente(paciente);
                atendimento.setProcedimento(procedimento);

                atendimentos.add(atendimento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar atendimentos: " + e.getMessage());
        }
        return atendimentos;
    }

    /**
     * Atualiza um atendimento de forma dinâmica. Apenas os campos não nulos no objeto de entrada serão atualizados.
     * @param atendimento O objeto Atendimento com o ID e os dados a serem modificados.
     */
    public void atualizar(Atendimento atendimento) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE atendimentos SET ");
        List<Object> params = new ArrayList<>();

        if (atendimento.getData() != null) {
            sqlBuilder.append("data = ?, ");
            params.add(atendimento.getData().format(DATE_FORMATTER));
        }
        if (atendimento.getHorario() != null && !atendimento.getHorario().isEmpty()) {
            sqlBuilder.append("horario = ?, ");
            params.add(atendimento.getHorario());
        }
        if (atendimento.getStatusP() != null) {
            sqlBuilder.append("status_pagamento = ?, ");
            params.add(atendimento.getStatusP().name());
        }
        if (atendimento.getDentista() != null && atendimento.getDentista().getId() > 0) {
            sqlBuilder.append("dentista_id = ?, ");
            params.add(atendimento.getDentista().getId());
        }
        if (atendimento.getPaciente() != null && atendimento.getPaciente().getId() > 0) {
            sqlBuilder.append("paciente_id = ?, ");
            params.add(atendimento.getPaciente().getId());
        }
        if (atendimento.getProcedimentos() != null && atendimento.getProcedimentos().getId() > 0) {
            sqlBuilder.append("procedimento_id = ?, ");
            params.add(atendimento.getProcedimentos().getId());
        }

        if (params.isEmpty()) {
            System.out.println("Nenhum campo fornecido para atualização do atendimento.");
            return;
        }

        sqlBuilder.setLength(sqlBuilder.length() - 2); // Remove a vírgula final
        sqlBuilder.append(" WHERE id = ?");
        params.add(atendimento.getId());

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Atendimento atualizado com sucesso!");
            } else {
                System.out.println("Nenhum atendimento encontrado com o ID fornecido.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar atendimento: " + e.getMessage());
        }
    }

    /**
     * Deleta um atendimento do banco de dados com base no seu ID.
     * @param id O ID do atendimento a ser deletado.
     */
    public void deletar(int id) {
        String sql = "DELETE FROM atendimentos WHERE id = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Atendimento deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar atendimento: " + e.getMessage());
        }
    }
}