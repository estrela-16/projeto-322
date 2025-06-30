package Principal;
/* interface do calendario e alteraçao  e criacao de atendimentos*/
import DAO.AtendimentoDAO;
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
    private List<Paciente> pacientes;
    private List<Dentista> dentistas;
    private GerenciarProcedimento gerencia;
    private AtendimentoDAO atendimentoDAO;


    public AgendaUI(Agenda agenda, int mes, int ano, List<Paciente> pacientes, List<Dentista> dentistas,  GerenciarProcedimento gerencia, AtendimentoDAO atendimentoDAO) {
        this.agenda = agenda;
        this.mesAnoAtual = YearMonth.of(ano, mes);
        this.pacientes = pacientes;
        this.dentistas = dentistas;
        this.gerencia = gerencia;
        this.atendimentoDAO = atendimentoDAO;

        setLayout(new BorderLayout());

        JPanel topo = new JPanel(new BorderLayout());
        JButton botaoAnterior = new JButton("←");
        JButton botaoProximo = new JButton("→");

        labelMesAno = new JLabel("", SwingConstants.CENTER);
        labelMesAno.setFont(new Font("Arial", Font.BOLD, 20));

        topo.add(botaoAnterior, BorderLayout.WEST);
        topo.add(labelMesAno, BorderLayout.CENTER);
        topo.add(botaoProximo, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        painelDias = new JPanel(new GridLayout(0, 7, 5, 5));
        add(painelDias, BorderLayout.CENTER);

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

        // passar meses
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

            List<Atendimento> atendimentosDoDia = agenda.getTodos().stream()
            .filter(a -> a.getData().equals(dataBotao))
            .collect(Collectors.toList());

            if (atendimentosDoDia.isEmpty()) {
                // muda a cor do atendimento dependendo do seu status
                botaoDia.setBackground(new Color(173, 216, 230)); 
            } else if (atendimentosDoDia.stream().allMatch(a -> a.getStatusP() == StatusPagamento.PAGO)) {
                botaoDia.setBackground(Color.GREEN); 
            } else if (atendimentosDoDia.stream().anyMatch(a -> a.getStatusP() == StatusPagamento.NAO_PAGO)) {
                botaoDia.setBackground(Color.RED); 
            }

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
        } 
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("Atendimentos em ").append(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(":\n\n");
            for (Atendimento a : atendimentosDoDia) {
                sb.append("Horário: ").append(a.getHorario())
                  .append(" | Paciente: ").append(a.getPaciente().getNome())
                  .append(" | Dentista: ").append(a.getDentista().getNome())
                  .append(" | Procedimento: ").append(a.getProcedimentos().getNome())
                  .append(" | Pagamento: ").append(a.getStatusP())
                  .append("\n");
            }
            detalhesAtendimentos.setText(sb.toString());

            // Adiciona botão para alterar status de pagamento
            JButton botaoAlterar = new JButton("Alterar Status de Pagamento");
            botaoAlterar.addActionListener(e -> {
                Atendimento[] opcoes = atendimentosDoDia.toArray(new Atendimento[0]);
                Atendimento selecionado = (Atendimento) JOptionPane.showInputDialog(
                    this,
                    "Selecione o atendimento para alterar o status:",
                    "Alterar Status",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
                );

                if (selecionado != null) {
                    selecionado.mudarStatusDePagamento();
                    atualizarCalendario();
                    mostrarAtendimentosDoDia(selecionado.getData());
                }
            });

            JPanel painelBotao = new JPanel();
            painelBotao.add(botaoAlterar);
            JOptionPane.showMessageDialog(this, painelBotao, "Opções", JOptionPane.PLAIN_MESSAGE);
        }
    }


    private void criarNovoAtendimento() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        List<Procedimento> procedimentos =gerencia.getProcedimentos();

        JComboBox<Paciente> pacienteBox = new JComboBox<>(pacientes.toArray(new Paciente[0]));
        JComboBox<Dentista> dentistaBox = new JComboBox<>(dentistas.toArray(new Dentista[0]));
        JComboBox<Procedimento> procedimentoBox = new JComboBox<>(procedimentos.toArray(new Procedimento[0]));
        JTextField dataField = new JTextField();
        JTextField horaField = new JTextField();

        panel.add(new JLabel("Paciente:"));
        panel.add(pacienteBox);
        panel.add(new JLabel("Dentista:"));
        panel.add(dentistaBox);
        panel.add(new JLabel("Procedimento:"));
        panel.add(procedimentoBox);
        panel.add(new JLabel("Data (dd-MM-yyyy):"));
        panel.add(dataField);
        panel.add(new JLabel("Hora (HH:mm):"));
        panel.add(horaField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Novo Atendimento", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate data = LocalDate.parse(dataField.getText(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String hora = horaField.getText();
                Paciente paciente = (Paciente) pacienteBox.getSelectedItem();
                Dentista dentista = (Dentista) dentistaBox.getSelectedItem();
                Procedimento procedimento = (Procedimento) procedimentoBox.getSelectedItem();

                if (paciente == null || dentista == null || procedimento == null) {
                    JOptionPane.showMessageDialog(this, "Por favor, selecione paciente, dentista e procedimento válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Atendimento novo = new Atendimento(data, hora, procedimento, paciente, dentista,StatusPagamento.NAO_PAGO);

                atendimentoDAO.inserir(novo);
                agenda.agendarAtendimento(novo);
                atualizarCalendario();
                mostrarAtendimentosDoDia(data);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao criar atendimento: " + ex.getMessage());
            }
        }
    }
}
