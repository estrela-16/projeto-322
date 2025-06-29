package Principal;


import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class MateriaisUI extends JFrame {
   private List<Materiais> materiais;
   private DefaultTableModel tabelaModel;
   private JTable tabelaMateriais;


   public MateriaisUI(List<Materiais> materiais) {
       this.materiais = materiais;


       setTitle("Materiais");
       setSize(400, 300);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       setLocationRelativeTo(null);


       // Define o modelo da tabela com colunas "Nome" e "Valor"
       tabelaModel = new DefaultTableModel(new Object[]{"Nome", "Valor (R$)"}, 0) {
           @Override
           public boolean isCellEditable(int row, int column) {
               return false; // impede edição direta
           }
       };


       tabelaMateriais = new JTable(tabelaModel);
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
       atualizarTabela();
   }


   private void atualizarTabela() {
       tabelaModel.setRowCount(0); // limpa a tabela
       for (Materiais m : materiais) {
           tabelaModel.addRow(new Object[]{m.getNome(), String.format("%.2f", m.getValor())});
       }
   }
}


