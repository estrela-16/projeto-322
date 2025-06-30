package Principal;
/* painel principal que junta todas as interfaces */
import DAO.AtendimentoDAO;
import DAO.ContasDAO;
import DAO.DentistaDAO;
import DAO.MateriaisComunsDAO;
import DAO.MateriaisDAO;
import DAO.PacienteDAO;
import DAO.ProcedimentoDAO;
import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn; 

public class ClinicaOdontoGUI extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel pacientesPanel;
    private JPanel dentistasPanel;
    private JPanel agendaPanel;
    private JPanel procedimentosPanel;
    private JPanel financeiroPanel;
    private JPanel materiaisPanel;
    private JPanel geralPanel;
    private DefaultListModel<Materiais> materiaisListModel;
    private JList<Materiais> materiaisJList;

    private JTable pacientesTable;
    private DefaultTableModel pacientesTableModel;
    private JTable dentistasTable;
    private DefaultTableModel dentistasTableModel;
    private JTable materiaisTable;
    private DefaultTableModel materiaisTableModel;
    private JTable procedimentosTable;
    private DefaultTableModel procedimentosTableModel;

    private List<Paciente> pacientes;
    private List<Dentista> dentistas;
    private List<Materiais> materiais;
    private List<Contas> contas;
    private List<MateriaisComuns> materiaiscomuns;

    private GerenciarProcedimento gerenciarProcedimento;
    private Agenda agenda;
    private Financeiro financeiro;
    private CalculodeGastos gastos;

    //DAOs do banco de dados:
    private PacienteDAO pacienteDAO;
    private DentistaDAO dentistaDAO;
    private MateriaisDAO materiaisDAO;
    private ProcedimentoDAO procedimentoDAO;
    private AtendimentoDAO atendimentoDAO;
    private ContasDAO contasDAO;
    private MateriaisComunsDAO materiaisComunsDAO;

    public ClinicaOdontoGUI() {
        super("Sistema de Gerenciamento Odontológico");
        CriadorTabelas.criarTabelas();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // INICIALIZAÇÃO DOS DAOs 
        this.pacienteDAO = new PacienteDAO();
        this.dentistaDAO = new DentistaDAO();
        this.materiaisDAO = new MateriaisDAO();
        this.procedimentoDAO = new ProcedimentoDAO();
        this.atendimentoDAO = new AtendimentoDAO();
        this.contasDAO = new ContasDAO();
        this.materiaisComunsDAO = new MateriaisComunsDAO();

        //CARREGAMENTO DOS DADOS DO BANCO 
        pacientes = pacienteDAO.buscarTodos();
        dentistas = dentistaDAO.buscarTodos();
        materiais = materiaisDAO.buscarTodos();
        contas = contasDAO.buscarTodos();
        materiaiscomuns = materiaisComunsDAO.buscarTodos();

        // INICIALIZAÇÃO DOS OBJETOs
        agenda = new Agenda();
        agenda.getTodos().addAll(atendimentoDAO.buscarTodos());
        
        gerenciarProcedimento = new GerenciarProcedimento();
        gerenciarProcedimento.getProcedimentos().addAll(procedimentoDAO.buscarTodos());
        
        financeiro = new Financeiro(agenda, 0);
        
        gastos = new CalculodeGastos();
        gastos.setConta(contas);
        gastos.setMateriaisComunses(materiaiscomuns);

        // Vincula o 'gastos' aos procedimentos da lista principal
        for (Procedimento p : gerenciarProcedimento.getProcedimentos()) {
            p.setGastos(this.gastos);
        }
        
    
        // Garante que os procedimentos DENTRO dos atendimentos também tenham o 'gastos' vinculado.
        for (Atendimento a : agenda.getTodos()) {
            if (a.getProcedimentos() != null) {
                a.getProcedimentos().setGastos(this.gastos);
            }
        }

        //INTERFACE GRÁFICA 
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

        materiaisPanel = createMateriaisPanel();
        tabbedPane.addTab("Materiais", materiaisPanel);

        geralPanel = new GeralUI(contas, materiaiscomuns, this, gastos, contasDAO, materiaisComunsDAO);
        tabbedPane.addTab("Gastos Gerais", geralPanel);

        tabbedPane.addChangeListener(e -> {
        int selectedIndex = tabbedPane.getSelectedIndex();
        String selectedTitle = tabbedPane.getTitleAt(selectedIndex);
            if ("Procedimentos".equals(selectedTitle)) {
        atualizarListaMateriaisProcedimentos();
    }
});
    }
    
    private JPanel createProcedimentosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Procedimento", "Especialidade", "Materiais Utilizados","Valor Total (R$)"};
        procedimentosTableModel = new DefaultTableModel(colunas, 0);
        procedimentosTable = new JTable(procedimentosTableModel);
        JScrollPane scrollPane = new JScrollPane(procedimentosTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel cadastroPanel = new JPanel(new GridBagLayout());
        cadastroPanel.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo Procedimento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        var mapaDeProcedimentos = new java.util.HashMap<String, String[]>();
        mapaDeProcedimentos.put("Clínica Geral", new String[]{"Aplicação tópica de flúor", "Profilaxia", "Anamnese", "Restauração em resina", "Extração simples", "Tratamento de cáries iniciais"});
        mapaDeProcedimentos.put("Endodontia", new String[]{"Tratamento endodôntico", "Retratamento de canal", "Curativo endodôntico de urgência", "Pulpotomia"});
        mapaDeProcedimentos.put("Ortodontia", new String[]{"Avaliação ortodôntica", "Instalação de aparelho fixo", "Manutenção de aparelho ortodôntico", "Remoção de aparelho e contenção", "Instalação de aparelhos removíveis"});
        mapaDeProcedimentos.put("Implantodontia", new String[]{"Cirurgia de instalação de implantes", "Colocação de pilar cicatricial", "Prótese sobre implante", "Manutenção e acompanhamento de implantes"});

        JLabel especialidadeLabel = new JLabel("Especialidade:");
        JComboBox<String> especialidadeComboBox = new JComboBox<>(mapaDeProcedimentos.keySet().toArray(new String[0]));

        JLabel procedimentoLabel = new JLabel("Procedimento:");
        JComboBox<String> procedimentoComboBox = new JComboBox<>();

        especialidadeComboBox.addActionListener(e -> {
            String especialidadeSelecionada = (String) especialidadeComboBox.getSelectedItem();
            procedimentoComboBox.removeAllItems();
            if (especialidadeSelecionada != null) {
                String[] procedimentos = mapaDeProcedimentos.get(especialidadeSelecionada);
                for (String proc : procedimentos) {
                    procedimentoComboBox.addItem(proc);
                }
            }
        });
        especialidadeComboBox.setSelectedIndex(0);

        JLabel selecionarMateriaisLabel = new JLabel("Selecione os Materiais:");
        materiaisListModel = new DefaultListModel<>();
        for (Materiais mat : this.materiais) {
            materiaisListModel.addElement(mat);
        }
        materiaisJList = new JList<>(materiaisListModel);
        materiaisJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane materiaisScrollPane = new JScrollPane(materiaisJList);
        materiaisScrollPane.setPreferredSize(new Dimension(250, 100));

        JButton adicionarProcedimentoButton = new JButton("Adicionar Procedimento à Lista");
        adicionarProcedimentoButton.addActionListener(e -> {
            String especialidade = (String) especialidadeComboBox.getSelectedItem();
            String nomeProcedimento = (String) procedimentoComboBox.getSelectedItem();

            List<Materiais> materiaisSelecionados = materiaisJList.getSelectedValuesList();

            if (nomeProcedimento == null) {
                JOptionPane.showMessageDialog(this, "Selecione um procedimento válido.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Procedimento novoProcedimento = new Procedimento(nomeProcedimento, especialidade, this.gastos);
            for (Materiais materialSelecionado : materiaisSelecionados) {
                novoProcedimento.adicionarMaterial(materialSelecionado);
            }
            
            boolean sucesso = procedimentoDAO.inserir(novoProcedimento);

            if (sucesso) {
                gerenciarProcedimento.adicionarProcedimento(novoProcedimento);
                atualizarTabelaProcedimentos();
                materiaisJList.clearSelection();
                JOptionPane.showMessageDialog(this, "Procedimento '" + nomeProcedimento + "' adicionado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Não foi possível adicionar o procedimento.\n",
                    "Erro de Duplicidade",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_END; cadastroPanel.add(especialidadeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START; cadastroPanel.add(especialidadeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.anchor = GridBagConstraints.LINE_END; cadastroPanel.add(procedimentoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START; cadastroPanel.add(procedimentoComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.anchor = GridBagConstraints.NORTHEAST; cadastroPanel.add(selecionarMateriaisLabel, gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.anchor = GridBagConstraints.LINE_START; cadastroPanel.add(materiaisScrollPane, gbc);

        gbc.gridx = 1; gbc.gridy = ++y; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; cadastroPanel.add(adicionarProcedimentoButton, gbc);

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        atualizarTabelaProcedimentos();
        return panel;
    }

private void atualizarTabelaProcedimentos() {
    procedimentosTableModel.setRowCount(0);

    for (Procedimento p : gerenciarProcedimento.getProcedimentos()) {
        List<Materiais> materiaisDoProcedimento = p.getMateriais();
        String nomesMateriais = materiaisDoProcedimento.stream()
                                  .map(Materiais::getNome)
                                  .collect(Collectors.joining(", "));
        
        double custoFinal = p.calcularCustoTotal(); // Já inclui materiais e taxa

        procedimentosTableModel.addRow(new Object[]{
            p.getNome(),
            p.getEspecialidade(),
            nomesMateriais,
            String.format("R$ %.2f", custoFinal)
        });
    }
}

    public void atualizarComissaoFinanceiro(double novaComissao) {
        financeiro.setPercentualComissao(novaComissao);
    }
    private JPanel createFinanceiroPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel anoLabel = new JLabel("Ano:");
        JTextField anoField = new JTextField(4);
        JButton gerarRelatorioButton = new JButton("Gerar Relatório Anual");

        inputPanel.add(anoLabel);
        inputPanel.add(anoField);
        inputPanel.add(gerarRelatorioButton);
        panel.add(inputPanel, BorderLayout.NORTH);

        JTextArea relatorioArea = new JTextArea(20, 50);
        relatorioArea.setEditable(false);
        Font fonteAtual = relatorioArea.getFont();
        relatorioArea.setFont(fonteAtual.deriveFont(22f)); 

        JScrollPane scrollPane = new JScrollPane(relatorioArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        gerarRelatorioButton.addActionListener(e -> {
            try {
                int ano = Integer.parseInt(anoField.getText());
                String relatorio = financeiro.gerarRelatorioMensal(ano);
                relatorioArea.setText(relatorio);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um ano válido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createPacientesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)); 

        String[] colunas = {"Nome", "Telefone", "CPF"};
        pacientesTableModel = new DefaultTableModel(colunas, 0) {
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pacientesTable = new JTable(pacientesTableModel);
        JScrollPane scrollPane = new JScrollPane(pacientesTable);

        
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton verHistoricoButton = new JButton("Ver/Editar Histórico");
        botoesPanel.add(verHistoricoButton);
        
        // Adiciona os componentes ao painel principal
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(botoesPanel, BorderLayout.NORTH); 

        // Ação do botão para ver o histórico
        verHistoricoButton.addActionListener(e -> {
            int selectedRow = pacientesTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Pega o paciente selecionado na tabela
                Paciente pacienteSelecionado = pacientes.get(selectedRow);

                // Abre a nova janela de histórico para o paciente
                HistoricoGUI historicoGUI = new HistoricoGUI(pacienteSelecionado);
                historicoGUI.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um paciente na tabela primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

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

            Paciente novoPaciente = new Paciente(nome, cpf, telefone);
            pacienteDAO.inserir(novoPaciente);
            pacientes.add(novoPaciente);
            atualizarTabelaPacientes();
            nomeField.setText("");
            telefoneField.setText("");
            cpfField.setText("");
        });

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        atualizarTabelaPacientes();
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
        
        dentistasTableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (row >= 0 && column >= 0 && row < dentistas.size()){
                Dentista d = dentistas.get(row);
                Object newValue = dentistasTableModel.getValueAt(row, column);

                switch (column) {
                    case 0: d.setNome((String) newValue); break;
                    case 1: d.setTelefone((String) newValue); break;
                    case 2: d.setCpf((String) newValue); break;
                    case 3: d.setCro((String) newValue); break;
                }
                dentistaDAO.atualizar(d);
            }
        });
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
 
           Dentista novoDentista = new Dentista(nome, cpf, telefone, cro);

           dentistaDAO.inserir(novoDentista);

           dentistas.add(novoDentista);
            atualizarTabelaDentistas();
            nomeField.setText(""); telefoneField.setText(""); cpfField.setText(""); croField.setText("");
        });

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        atualizarTabelaDentistas();
        return panel;
    }

    private void atualizarTabelaDentistas() {
        dentistasTableModel.setRowCount(0);
        for (Dentista d : dentistas) {
            dentistasTableModel.addRow(new Object[]{d.getNome(), d.getTelefone(), d.getCpf(), d.getCro()});
        }
    }
    
    private JPanel createAgendaPanel() {
        return new AgendaUI(agenda, LocalDate.now().getMonthValue(), LocalDate.now().getYear(), pacientes, dentistas, gerenciarProcedimento, atendimentoDAO);
    }

    private JPanel createMateriaisPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        materiaisTableModel = new DefaultTableModel(new Object[]{"Nome", "Valor (R$)", ""}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        materiaisTable = new JTable(materiaisTableModel);

        TableColumn colunaBotao = materiaisTable.getColumnModel().getColumn(2);
        colunaBotao.setPreferredWidth(30);
        colunaBotao.setMaxWidth(30);
        colunaBotao.setMinWidth(30);

        colunaBotao.setCellRenderer(new ButtonRenderer());
        colunaBotao.setCellEditor(new ButtonEditor(new JCheckBox()));

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
                Materiais novoMaterial = new Materiais (nome, valor);
                materiaisDAO.inserir(novoMaterial);
                materiais.add(new Materiais(nome, valor));
                materiais.sort(Comparator.comparing(Materiais::getNome));
                atualizarTabelaMateriais();
                nomeField.setText(""); valorField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
            }
        });

        panel.add(cadastroPanel, BorderLayout.SOUTH);
        atualizarTabelaMateriais();
        return panel;
    }
    
    private void atualizarTabelaMateriais() {
        materiaisTableModel.setRowCount(0);
        materiais = materiaisDAO.buscarTodos(); 
        for (Materiais m : materiais) {
            materiaisTableModel.addRow(new Object[]{m.getNome(), m.getValor(), "⋮"});
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("⋮");
            setFont(new Font("SansSerif", Font.PLAIN, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private void atualizarListaMateriaisProcedimentos() {
    if (materiaisListModel == null) return;
    materiaisListModel.clear();
    for (Materiais mat : materiais) {
        materiaisListModel.addElement(mat);
    }
}

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("⋮");
            button.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                int resposta = JOptionPane.showOptionDialog(
                    button, "Remover este material?", "Confirmação",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Sim", "Não"}, "Não"
                );

                if (resposta == JOptionPane.YES_OPTION && row < materiais.size()) {
                    Materiais materialSelecionado = materiais.get(row);

                    boolean estaEmUso = gerenciarProcedimento.getProcedimentos().stream()
                        .anyMatch(p -> p.getMateriais().contains(materialSelecionado));

                    if (estaEmUso) {
                        JOptionPane.showMessageDialog(button,
                            "Este material está sendo usado em um procedimento e não pode ser removido.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    } else {
                        materiaisDAO.deletar(materialSelecionado.getId());
                        materiais.remove(row);
                        atualizarTabelaMateriais();
                        atualizarListaMateriaisProcedimentos(); // também atualiza a JList de materiais
                    }
                }
            }
            clicked = false;
            return "⋮";
        }


        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClinicaOdontoGUI::new);
    }
}