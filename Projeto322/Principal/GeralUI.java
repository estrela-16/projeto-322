package Principal;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class GeralUI extends JPanel {
    private List<Contas> contas;
    private List<MateriaisComuns> materiaisComuns;

    private DefaultTableModel contasModel;
    private DefaultTableModel materiaisModel;

    public GeralUI(List<Contas> contas, List<MateriaisComuns> materiaisComuns) {
        this.contas = contas;
        this.materiaisComuns = materiaisComuns;

        setLayout(new BorderLayout());

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        // Painel de configurações
        JPanel configPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        configPanel.setBorder(BorderFactory.createTitledBorder("Configurações de Cálculo"));

        JTextField consultasField = new JTextField(5);
        JTextField comissaoField = new JTextField(5);
        JTextField taxaServicoField = new JTextField(5);
        JButton salvarBtn = new JButton("Salvar");

        configPanel.add(new JLabel("Consultas no mês:"));
        configPanel.add(consultasField);
        configPanel.add(new JLabel("Comissão (%):"));
        configPanel.add(comissaoField);
        configPanel.add(new JLabel("Taxa de Serviço (%):"));
        configPanel.add(taxaServicoField);
        configPanel.add(salvarBtn);

        salvarBtn.addActionListener(e -> {
            try {
                int consultas = Integer.parseInt(consultasField.getText());
                double comissao = Double.parseDouble(comissaoField.getText()) / 100.0;
                double taxaServico = Double.parseDouble(taxaServicoField.getText()) / 100.0;

                CalculodeGastos calculo = new CalculodeGastos();
                calculo.getMateriaisComunses().addAll(materiaisComuns);
                calculo.getConta().addAll(contas);
                calculo.setNumeroDeConsultas(consultas);
                calculo.setComissao(comissao);
                calculo.setTaxaServico(taxaServico);

                // Salvo internamente - sem exibir resultado
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Insira valores válidos para consultas, comissão e taxa de serviço.");
            }
        });

        conteudo.add(configPanel);
        conteudo.add(Box.createVerticalStrut(10));
        conteudo.add(criarPainelContas());
        conteudo.add(Box.createVerticalStrut(20));
        conteudo.add(criarPainelMateriais());

        JScrollPane scrollPane = new JScrollPane(conteudo);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        atualizarTabelaContas();
        atualizarTabelaMateriais();
    }

    private JPanel criarPainelContas() {
        JPanel contasPanel = new JPanel(new BorderLayout());
        contasPanel.setBorder(BorderFactory.createTitledBorder("Contas"));

        contasModel = new DefaultTableModel(new Object[]{"Nome", "Valor (R$)", ""}, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 2;
            }
        };
        JTable contasTable = new JTable(contasModel);
        TableColumn colRemover = contasTable.getColumnModel().getColumn(2);
        colRemover.setCellRenderer(new ButtonRenderer());
        colRemover.setCellEditor(new ButtonEditor(new JCheckBox(), contas, contasModel));
        colRemover.setMaxWidth(40);
        JScrollPane scrollContas = new JScrollPane(contasTable);
        scrollContas.setPreferredSize(new Dimension(600, 150));
        contasPanel.add(scrollContas, BorderLayout.CENTER);

        JPanel inputContas = new JPanel(new FlowLayout());
        JTextField nomeContaField = new JTextField(15);
        JTextField valorContaField = new JTextField(8);
        JButton addContaBtn = new JButton("Adicionar Conta");
        inputContas.add(new JLabel("Nome:"));
        inputContas.add(nomeContaField);
        inputContas.add(new JLabel("Valor:"));
        inputContas.add(valorContaField);
        inputContas.add(addContaBtn);
        contasPanel.add(inputContas, BorderLayout.SOUTH);

        addContaBtn.addActionListener(e -> {
            String nome = nomeContaField.getText();
            String valorTexto = valorContaField.getText();
            if (nome.isEmpty() || valorTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
            try {
                double valor = Double.parseDouble(valorTexto);
                contas.add(new Contas(nome, valor));
                atualizarTabelaContas();
                nomeContaField.setText("");
                valorContaField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
            }
        });

        return contasPanel;
    }

    private JPanel criarPainelMateriais() {
        JPanel materiaisPanel = new JPanel(new BorderLayout());
        materiaisPanel.setBorder(BorderFactory.createTitledBorder("Materiais Básicos"));

        materiaisModel = new DefaultTableModel(new Object[]{"Nome", "Valor (R$)", "Quantidade", ""}, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 3;
            }
        };

        JTable materiaisTable = new JTable(materiaisModel);
        TableColumn colRemover = materiaisTable.getColumnModel().getColumn(3);
        colRemover.setCellRenderer(new ButtonRenderer());
        colRemover.setCellEditor(new ButtonEditorMateriais(new JCheckBox(), materiaisComuns, materiaisModel));
        colRemover.setMaxWidth(40);
        materiaisPanel.add(new JScrollPane(materiaisTable), BorderLayout.CENTER);

        JPanel inputMateriais = new JPanel(new FlowLayout());
        JTextField nomeMaterialField = new JTextField(12);
        JTextField valorMaterialField = new JTextField(6);
        JTextField qtdField = new JTextField(4);
        JButton addMaterialBtn = new JButton("Adicionar Material");

        inputMateriais.add(new JLabel("Nome:"));
        inputMateriais.add(nomeMaterialField);
        inputMateriais.add(new JLabel("Valor:"));
        inputMateriais.add(valorMaterialField);
        inputMateriais.add(new JLabel("Qtd:"));
        inputMateriais.add(qtdField);
        inputMateriais.add(addMaterialBtn);

        materiaisPanel.add(inputMateriais, BorderLayout.SOUTH);

        addMaterialBtn.addActionListener(e -> {
            String nome = nomeMaterialField.getText();
            String valorTexto = valorMaterialField.getText();
            String qtdTexto = qtdField.getText();
            if (nome.isEmpty() || valorTexto.isEmpty() || qtdTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
            try {
                double valor = Double.parseDouble(valorTexto);
                int qtd = Integer.parseInt(qtdTexto);
                materiaisComuns.add(new MateriaisComuns(nome, valor, qtd));
                atualizarTabelaMateriais();
                nomeMaterialField.setText("");
                valorMaterialField.setText("");
                qtdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor ou quantidade inválido.");
            }
        });

        return materiaisPanel;
    }

    private void atualizarTabelaContas() {
        contasModel.setRowCount(0);
        for (Contas c : contas) {
            contasModel.addRow(new Object[]{c.getNome(), c.getValor(), "⋮"});
        }
    }

    private void atualizarTabelaMateriais() {
        materiaisModel.setRowCount(0);
        for (MateriaisComuns m : materiaisComuns) {
            materiaisModel.addRow(new Object[]{m.getNome(), m.getValor(), m.getQuantidade(), "⋮"});
        }
    }

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("\u22ee");
            setFont(new Font("SansSerif", Font.PLAIN, 14));
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int row;
        private List<Contas> contas;
        private DefaultTableModel model;

        public ButtonEditor(JCheckBox checkBox, List<Contas> contas, DefaultTableModel model) {
            super(checkBox);
            this.contas = contas;
            this.model = model;
            button = new JButton("\u22ee");
            button.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            this.row = row;
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                int opcao = JOptionPane.showConfirmDialog(button, "Remover esta conta?", "Confirmação",
                        JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION && row < contas.size()) {
                    contas.remove(row);
                    model.removeRow(row);
                }
            }
            clicked = false;
            return "⋮";
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    private static class ButtonEditorMateriais extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int row;
        private List<MateriaisComuns> materiais;
        private DefaultTableModel model;

        public ButtonEditorMateriais(JCheckBox checkBox, List<MateriaisComuns> materiais, DefaultTableModel model) {
            super(checkBox);
            this.materiais = materiais;
            this.model = model;
            button = new JButton("\u22ee");
            button.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            this.row = row;
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                int opcao = JOptionPane.showConfirmDialog(button, "Remover este material?", "Confirmação",
                        JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION && row < materiais.size()) {
                    materiais.remove(row);
                    model.removeRow(row);
                }
            }
            clicked = false;
            return "⋮";
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
