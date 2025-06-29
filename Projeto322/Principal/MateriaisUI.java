package Principal;


import java.awt.*;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class MateriaisUI extends JFrame {
   private List<Materiais> materiais;
   private DefaultTableModel tabelaModel;
   private JTable tabelaMateriais;

   public MateriaisUI(List<Materiais> materiais) {
       this.materiais = materiais;

       setTitle("Materiais");
       setSize(450, 300);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       setLocationRelativeTo(null);

       // Adiciona coluna extra "Remover"
        tabelaModel = new DefaultTableModel(new Object[]{"Nome", "Valor (R$)", ""}, 0) {
           @Override
           public boolean isCellEditable(int row, int column) {
               return column == 2; // só o botão da coluna 2 é editável (para clicar)
           }
       };

       tabelaMateriais = new JTable(tabelaModel);

       // Define renderizador do botão
       tabelaMateriais.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
       // Define editor do botão (para tratar clique)
       tabelaMateriais.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

       JScrollPane scroll = new JScrollPane(tabelaMateriais);

       JButton botaoNovo = new JButton("➕ Novo Material");
       botaoNovo.addActionListener(e -> adicionarMaterial());

       JPanel painel = new JPanel(new BorderLayout());
       painel.add(scroll, BorderLayout.CENTER);
       painel.add(botaoNovo, BorderLayout.SOUTH);

       add(painel);
       atualizarTabela();
       setVisible(true);
   }

   private void adicionarMaterial() {
       String nome = JOptionPane.showInputDialog(this, "Nome do material:");
       if (nome == null || nome.trim().isEmpty()) return;

       String valorStr = JOptionPane.showInputDialog(this, "Valor do material:");
       double valor;
       try {
           valor = Double.parseDouble(valorStr);
       } catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Valor inválido.");
           return;
       }

       Materiais novo = new Materiais(nome, valor);
       materiais.add(novo);
       materiais.sort(Comparator.comparing(Materiais::getNome));
       atualizarTabela();
   }

   private void atualizarTabela() {
       tabelaModel.setRowCount(0); // limpa a tabela
       for (Materiais m : materiais) {
           tabelaModel.addRow(new Object[]{m.getNome(), String.format("%.2f", m.getValor()), "➖"});
       }
   }

   // Renderiza o botão "menos"
   private class ButtonRenderer extends JButton implements TableCellRenderer {
       public ButtonRenderer() {
           setOpaque(true);
       }

       @Override
       public Component getTableCellRendererComponent(JTable table, Object value,
               boolean isSelected, boolean hasFocus, int row, int column) {
           setText((value == null) ? "" : value.toString());
           return this;
       }
   }

   // Trata o clique no botão
   private class ButtonEditor extends DefaultCellEditor {
       private JButton button;
       private String label;
       private boolean clicked;
       private int row;

       public ButtonEditor(JCheckBox checkBox) {
           super(checkBox);
           button = new JButton();
           button.setOpaque(true);

           button.addActionListener(e -> fireEditingStopped());
       }

       @Override
       public Component getTableCellEditorComponent(JTable table, Object value,
               boolean isSelected, int row, int column) {
           this.row = row;
           label = (value == null) ? "" : value.toString();
           button.setText(label);
           clicked = true;
           return button;
       }

       @Override
       public Object getCellEditorValue() {
           if (clicked) {
               // Confirma remoção
               int resposta = JOptionPane.showConfirmDialog(button, "Remover este material?", "Confirmação",
                       JOptionPane.YES_NO_OPTION);
               if (resposta == JOptionPane.YES_OPTION) {
                   materiais.remove(row);
                   atualizarTabela();
               }
           }
           clicked = false;
           return label;
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
}



