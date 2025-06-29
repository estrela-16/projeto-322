package Principal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel; // Para JTable

public class ClinicaOdontoGUI extends JFrame {

    private JTabbedPane tabbedPane; 
    private JPanel pacientesPanel;
    private JPanel dentistasPanel;
    private JPanel agendaPanel;
    private JPanel procedimentosPanel;
    private JPanel financeiroPanel;

    private JTable pacientesTable;
    private DefaultTableModel pacientesTableModel;
    private JTable dentistasTable;
    private DefaultTableModel dentistasTableModel;
    private JTable procedimentosTable;
    private DefaultTableModel procedimentosTableModel;
    private JTable agendaTable;
    private DefaultTableModel agendaTableModel;

    private List<Paciente> pacientes;
    private List<Dentista> dentistas;
    private GerenciarProcedimento gerenciarProcedimento;
    private Agenda agenda;
    private Financeiro financeiro;


    public ClinicaOdontoGUI() {
        super("Sistema de Gerenciamento Odontológico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null); 

        pacientes = new ArrayList<>();
        dentistas = new ArrayList<>();

        tabbedPane = new JTabbedPane();
        createMenuBar(); 
        createTabs();

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createMenuBar() {
            JMenuBar menuBar = new JMenuBar();
            JMenu arquivoMenu = new JMenu("Menu");
            JMenuItem sairItem = new JMenuItem("Sair");

            sairItem.addActionListener(e -> System.exit(0));

            arquivoMenu.add(sairItem);
            menuBar.add(arquivoMenu);
            setJMenuBar(menuBar);
    }

    private void createTabs() {
        pacientesPanel = createPacientesPanel();
        tabbedPane.addTab("Pacientes", pacientesPanel);

        dentistasPanel = createDentistasPanel();
        tabbedPane.addTab("Dentistas", dentistasPanel);

        agendaPanel = createAgendaPanel();
        tabbedPane.addTab("Agenda", agendaPanel);

        procedimentosPanel = createProcedimentosPanel();
        tabbedPane.addTab("Procedimentos", procedimentosPanel);

        financeiroPanel = createFinanceiroPanel();
        tabbedPane.addTab("Financeiro", financeiroPanel);
    }

    private JPanel createDentistasPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabela de Dentistas
        String[] colunas = {"Nome", "Telefone", "CPF", "CRO"};
        dentistasTableModel = new DefaultTableModel(colunas, 0);
        dentistasTable = new JTable(dentistasTableModel);
        JScrollPane scrollPane = new JScrollPane(dentistasTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Painel de Cadastro de Dentistas
        JPanel cadastroPanel = new JPanel(new GridBagLayout());
        cadastroPanel.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo Dentista"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField(20);
        JLabel telefoneLabel = new JLabel("Telefone:");
        JTextField telefoneField = new JTextField(20);
         JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfField = new JTextField(20);
        JLabel croLabel = new JLabel("CRO:");
        JTextField croField = new JTextField(20);
        JButton cadastrarButton = new JButton("Cadastrar Dentista");

        gbc.gridx = 0; gbc.gridy = 0; cadastroPanel.add(nomeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; cadastroPanel.add(nomeField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; cadastroPanel.add(telefoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; cadastroPanel.add(telefoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; cadastroPanel.add(cpfLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; cadastroPanel.add(cpfField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; cadastroPanel.add(croLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; cadastroPanel.add(croField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; cadastroPanel.add(cadastrarButton, gbc);

        cadastrarButton.addActionListener(e -> {
            String nome = nomeField.getText();
            String telefone = telefoneField.getText();
            String cpf = cpfField.getText();
            String cro = croField.getText();

            if (nome.isEmpty() || telefone.isEmpty() || cro.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos para o dentista.", "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Dentista novoDentista = new Dentista(nome, telefone, cpf, cro);
            dentistas.add(novoDentista);
            atualizarTabelaDentistas();
            nomeField.setText("");
            telefoneField.setText("");
            cpfField.setText("");
            croField.setText("");
            JOptionPane.showMessageDialog(this, "Dentista cadastrado com sucesso!");
        });

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void atualizarTabelaDentistas() {
        dentistasTableModel.setRowCount(0); // Limpa a tabela
        for (Dentista d : dentistas) {
            dentistasTableModel.addRow(new Object[]{d.getNome(), d.getTelefone(), d.getCpf(), d.getCro()});
        }
    }

    private JPanel createAgendaPanel() {
        return new JPanel(new BorderLayout());
    }

    private JPanel createProcedimentosPanel() {
        return new JPanel(new BorderLayout());
    }

    private JPanel createFinanceiroPanel() {
        return new JPanel(new BorderLayout());
    }

    private JPanel createPacientesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabela de Pacientes
        String[] colunas = {"Nome", "Telefone", "CPF"};
        pacientesTableModel = new DefaultTableModel(colunas, 0);
        pacientesTable = new JTable(pacientesTableModel);
        JScrollPane scrollPane = new JScrollPane(pacientesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Painel de Cadastro de Pacientes
        JPanel cadastroPanel = new JPanel(new GridBagLayout());
        cadastroPanel.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo Paciente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField(20);
        JLabel telefoneLabel = new JLabel("Telefone:");
        JTextField telefoneField = new JTextField(20);
        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfField = new JTextField(20);
        JButton cadastrarButton = new JButton("Cadastrar Paciente");

        gbc.gridx = 0; gbc.gridy = 0; cadastroPanel.add(nomeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; cadastroPanel.add(nomeField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; cadastroPanel.add(telefoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; cadastroPanel.add(telefoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; cadastroPanel.add(cpfLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; cadastroPanel.add(cpfField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; cadastroPanel.add(cadastrarButton, gbc);

        cadastrarButton.addActionListener(e -> {
            String nome = nomeField.getText();
            String telefone = telefoneField.getText();
            String cpf = cpfField.getText();

            if (nome.isEmpty() || telefone.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos para o paciente.", "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Paciente novoPaciente = new Paciente(nome, telefone, cpf);
            pacientes.add(novoPaciente);
            atualizarTabelaPacientes(); // Atualiza a tabela com o novo paciente
            nomeField.setText("");
            telefoneField.setText("");
            cpfField.setText("");
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
        });

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void atualizarTabelaPacientes() {
        pacientesTableModel.setRowCount(0); // Limpa a tabela
        for (Paciente p : pacientes) {
            pacientesTableModel.addRow(new Object[]{p.getNome(), p.getTelefone(), p.getCpf()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClinicaOdontoGUI());
    }
}
