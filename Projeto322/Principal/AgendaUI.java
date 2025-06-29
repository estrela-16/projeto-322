package Principal;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.swing.*;

public class AgendaUI extends JPanel { // ALTERADO de JFrame para JPanel
    private Agenda agenda;
    private YearMonth mesAnoAtual;
    private JPanel painelDias;
    private JTextArea detalhesAtendimentos;
    private JLabel labelMesAno;

    public AgendaUI(Agenda agenda, int mes, int ano) {
        this.agenda = agenda;
        this.mesAnoAtual = YearMonth.of(ano, mes);

        setLayout(new BorderLayout());

        // Topo com mês, ano e botões de navegação
        JPanel topo = new JPanel(new BorderLayout());
        JButton botaoAnterior = new JButton("←");
        JButton botaoProximo = new JButton("→");

        labelMesAno = new JLabel("", SwingConstants.CENTER);
        labelMesAno.setFont(new Font("Arial", Font.BOLD, 20));

        topo.add(botaoAnterior, BorderLayout.WEST);
        topo.add(labelMesAno, BorderLayout.CENTER);
        topo.add(botaoProximo, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        // Painel dos dias do mês
        painelDias = new JPanel(new GridLayout(0, 7, 5, 5));
        add(painelDias, BorderLayout.CENTER);

        // Painel inferior com botão + área de detalhes
        JPanel painelInferior = new JPanel(new BorderLayout());

        JButton botaoNovoAtendimento = new JButton("<html><center>+");
        botaoNovoAtendimento.setFont(new Font("Arial", Font.PLAIN, 40));
        botaoNovoAtendimento.setPreferredSize(new Dimension(100, 100));
        botaoNovoAtendimento.addActionListener(e -> criarNovoAtendimento());

        detalhesAtendimentos = new JTextArea();
        detalhesAtendimentos.setEditable(false);
        JScrollPane scroll = new JScrollPane(detalhesAtendimentos);

        painelInferior.add(botaoNovoAtendimento, BorderLayout.WEST);
        painelInferior.add(scroll, BorderLayout.CENTER);

        add(painelInferior, BorderLayout.SOUTH);

        // Navegação dos meses
        botaoAnterior.addActionListener(e -> {
            mesAnoAtual = mesAnoAtual.minusMonths(1);
            atualizarCalendario();
        });

        botaoProximo.addActionListener(e -> {
            mesAnoAtual = mesAnoAtual.plusMonths(1);
            atualizarCalendario();
        });

        atualizarCalendario();
    }

    public void atualizarCalendario() {
        painelDias.removeAll();

        String nomeMes = mesAnoAtual.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
        labelMesAno.setText(nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1) + " " + mesAnoAtual.getYear());

        int diasNoMes = mesAnoAtual.lengthOfMonth();
        for (int dia = 1; dia <= diasNoMes; dia++) {
            JButton botaoDia = new JButton(String.valueOf(dia));
            LocalDate dataBotao = mesAnoAtual.atDay(dia);

            boolean temAtendimento = agenda.getTodos().stream()
                .anyMatch(a -> a.getData().equals(dataBotao));

            botaoDia.setBackground(temAtendimento ? Color.CYAN : Color.LIGHT_GRAY);
            botaoDia.addActionListener(e -> mostrarAtendimentosDoDia(dataBotao));

            painelDias.add(botaoDia);
        }

        painelDias.revalidate();
        painelDias.repaint();
    }

    private void mostrarAtendimentosDoDia(LocalDate data) {
        List<Atendimento> atendimentosDoDia = agenda.getTodos().stream()
            .filter(a -> a.getData().equals(data))
            .collect(Collectors.toList());

        if (atendimentosDoDia.isEmpty()) {
            detalhesAtendimentos.setText("Nenhum atendimento para " + data);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Atendimentos em ").append(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(":\n\n");
            for (Atendimento a : atendimentosDoDia) {
                sb.append("Horário: ").append(a.getHorario())
                  .append(" | Paciente: ").append(a.getPaciente().getNome())
                  .append(" | Dentista: ").append(a.getDentista().getNome())
                  .append(" | Procedimento: ").append(a.getProcedimentos().getNome())
                  .append("\n");
            }
            detalhesAtendimentos.setText(sb.toString());
        }
    }

    private void criarNovoAtendimento() {
        try {
            String nomePaciente = JOptionPane.showInputDialog(this, "Nome do paciente:");
            if (nomePaciente == null || nomePaciente.isBlank()) return;

            String dataStr = JOptionPane.showInputDialog(this, "Data (DD-MM-AAAA):");
            if (dataStr == null || dataStr.isBlank()) return;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate data = LocalDate.parse(dataStr, formatter);

            String horario = JOptionPane.showInputDialog(this, "Horário (HH:mm):");
            if (horario == null || horario.isBlank()) return;

            String nomeProcedimento = JOptionPane.showInputDialog(this, "Nome do procedimento:");
            if (nomeProcedimento == null || nomeProcedimento.isBlank()) return;

            String nomeDentista = JOptionPane.showInputDialog(this, "Nome do dentista:");
            if (nomeDentista == null || nomeDentista.isBlank()) return;

            Paciente paciente = new Paciente(nomePaciente, "", "");
            Dentista dentista = new Dentista(nomeDentista, "", "", "");
            Procedimento procedimento = new Procedimento(nomeProcedimento, "");

            Atendimento novo = new Atendimento(
                data, horario, procedimento, paciente,
                dentista, StatusConsulta.AGENDADA, StatusPagamento.NAO_PAGA
            );

            agenda.agendarAtendimento(novo);
            atualizarCalendario();
            mostrarAtendimentosDoDia(data);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar atendimento: " + ex.getMessage());
        }
    }
}
