package Principal;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClinicaOdontoGUI extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel pacientesPanel;
    private JPanel dentistasPanel;
    private JPanel agendaPanel;
    private JPanel procedimentosPanel;
    private JPanel financeiroPanel;
    private JPanel materiaisPanel;

    private JTable pacientesTable;
    private DefaultTableModel pacientesTableModel;
    private JTable dentistasTable;
    private DefaultTableModel dentistasTableModel;
    private JTable materiaisTable;
    private DefaultTableModel materiaisTableModel;

    private List<Paciente> pacientes;
    private List<Dentista> dentistas;
    private List<Material> materiais;

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
        materiais = new ArrayList<>();
        financeiro = new Financeiro(agenda, 0.3);

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

        agendaPanel = new JPanel();
        tabbedPane.addTab("Agenda", agendaPanel);

        procedimentosPanel = new JPanel();
        tabbedPane.addTab("Procedimentos", procedimentosPanel);

        financeiroPanel = new JPanel();
        tabbedPane.addTab("Financeiro", financeiroPanel);

        materiaisPanel = createMateriaisPanel();
        tabbedPane.addTab("Materiais", materiaisPanel);
    }

    private JPanel createPacientesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] colunas = {"Nome", "Telefone", "CPF"};
        pacientesTableModel = new DefaultTableModel(colunas, 0);
        pacientesTable = new JTable(pacientesTableModel);
        JScrollPane scrollPane = new JScrollPane(pacientesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

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
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }

            pacientes.add(new Paciente(nome, cpf, telefone));
            atualizarTabelaPacientes();
            nomeField.setText(""); telefoneField.setText(""); cpfField.setText("");
        });

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void atualizarTabelaPacientes() {
        pacientesTableModel.setRowCount(0);
        for (Paciente p : pacientes) {
            pacientesTableModel.addRow(new Object[]{p.getNome(), p.getTelefone(), p.getCpf()});
        }
    }

    private JPanel createDentistasPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] colunas = {"Nome", "Telefone", "CPF", "CRO"};
        dentistasTableModel = new DefaultTableModel(colunas, 0);
        dentistasTable = new JTable(dentistasTableModel);
        JScrollPane scrollPane = new JScrollPane(dentistasTable);
        panel.add(scrollPane, BorderLayout.CENTER);

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
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }

            dentistas.add(new Dentista(nome, cpf, telefone, cro));
            atualizarTabelaDentistas();
            nomeField.setText(""); telefoneField.setText(""); cpfField.setText(""); croField.setText("");
        });

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void atualizarTabelaDentistas() {
        dentistasTableModel.setRowCount(0);
        for (Dentista d : dentistas) {
            dentistasTableModel.addRow(new Object[]{d.getNome(), d.getTelefone(), d.getCpf(), d.getCro()});
        }
    }

    private JPanel createMateriaisPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] colunas = {"Nome", "Valor (R$)"};
        materiaisTableModel = new DefaultTableModel(colunas, 0);
        materiaisTable = new JTable(materiaisTableModel);
        JScrollPane scrollPane = new JScrollPane(materiaisTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel cadastroPanel = new JPanel(new GridBagLayout());
        cadastroPanel.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo Material"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField(20);
        JLabel valorLabel = new JLabel("Valor (R$):");
        JTextField valorField = new JTextField(10);
        JButton cadastrarButton = new JButton("Cadastrar Material");

        gbc.gridx = 0; gbc.gridy = 0; cadastroPanel.add(nomeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; cadastroPanel.add(nomeField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; cadastroPanel.add(valorLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; cadastroPanel.add(valorField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; cadastroPanel.add(cadastrarButton, gbc);

        cadastrarButton.addActionListener(e -> {
            String nome = nomeField.getText();
            String valorTexto = valorField.getText();
            if (nome.isEmpty() || valorTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
            try {
                double valor = Double.parseDouble(valorTexto);
                materiais.add(new Material(nome, valor));
                atualizarTabelaMateriais();
                nomeField.setText(""); valorField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
            }
        });

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void atualizarTabelaMateriais() {
        materiaisTableModel.setRowCount(0);
        for (Material m : materiais) {
            materiaisTableModel.addRow(new Object[]{m.getNome(), m.getValor()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClinicaOdontoGUI::new);
    }
}

class Material {
    private String nome;
    private double valor;

    public Material(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }
    public String getNome() { return nome; }
    public double getValor() { return valor; }
    public void setNome(String nome) { this.nome = nome; }
    public void setValor(double valor) { this.valor = valor; }
}

class Paciente {
    private String nome, cpf, telefone;
    public Paciente(String nome, String cpf, String telefone) {
        this.nome = nome; this.cpf = cpf; this.telefone = telefone;
    }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}

class Dentista {
    private String nome, cpf, telefone, cro;
    public Dentista(String nome, String cpf, String telefone, String cro) {
        this.nome = nome; this.cpf = cpf; this.telefone = telefone; this.cro = cro;
    }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getCro() { return cro; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setCro(String cro) { this.cro = cro; }
}

class Financeiro {
    public Financeiro(Agenda agenda, double percentual) {}
}

class Agenda {}

class GerenciarProcedimento {}
