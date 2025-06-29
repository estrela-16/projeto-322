package Principal;

import DAO.HistoricoDAO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Uma classe de interface gráfica para testar a funcionalidade de
 * adicionar e visualizar imagens de um histórico de paciente de forma isolada.
 */
public class TesteAdicionarImagemGUI extends JFrame {

    // --- Componentes da Interface ---
    private JButton botaoAdicionarImagem;
    private JLabel labelVisualizador;
    private JList<String> listaCaminhos;
    private DefaultListModel<String> listModel;

    // --- Lógica de Dados ---
    private HistoricoDAO historicoDAO;
    private int idDoHistoricoParaTeste = 1;

    public TesteAdicionarImagemGUI() {
        super("Teste de Upload de Imagem para Histórico");
        this.historicoDAO = new HistoricoDAO();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTitulo = new JPanel();
        painelTitulo.add(new JLabel("Gerenciando Imagens para o Histórico ID: " + idDoHistoricoParaTeste));
        add(painelTitulo, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new GridLayout(1, 2, 10, 0));

        labelVisualizador = new JLabel("Selecione uma imagem da lista", SwingConstants.CENTER);
        labelVisualizador.setBorder(BorderFactory.createTitledBorder("Visualizador"));
        painelCentral.add(new JScrollPane(labelVisualizador));

        listModel = new DefaultListModel<>();
        listaCaminhos = new JList<>(listModel);
        JScrollPane painelLista = new JScrollPane(listaCaminhos);
        painelLista.setBorder(BorderFactory.createTitledBorder("Imagens Salvas no Banco"));
        painelCentral.add(painelLista);

        add(painelCentral, BorderLayout.CENTER);

        botaoAdicionarImagem = new JButton("Adicionar Nova Imagem");
        add(botaoAdicionarImagem, BorderLayout.SOUTH);

        botaoAdicionarImagem.addActionListener(e -> adicionarNovaImagemAoHistorico());

        listaCaminhos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String caminhoSelecionado = listaCaminhos.getSelectedValue();
                if (caminhoSelecionado != null) {
                    exibirImagem(new File(caminhoSelecionado));
                }
            }
        });

        carregarImagensSalvas();
    }

    private void carregarImagensSalvas() {
        listModel.clear();
        List<String> caminhos = historicoDAO.buscarImagens(idDoHistoricoParaTeste);
        if (caminhos.isEmpty()) {
            listModel.addElement("Nenhuma imagem salva para este histórico.");
        } else {
            for (String caminho : caminhos) {
                listModel.addElement(caminho);
            }
        }
    }

    private void adicionarNovaImagemAoHistorico() {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Selecione uma imagem");
        seletor.setFileFilter(new FileNameExtensionFilter("Imagens", "jpg", "png", "gif", "bmp"));

        if (seletor.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = seletor.getSelectedFile();
            try {
                Path pastaDestino = Paths.get("imagens_historico");
                if (!Files.exists(pastaDestino)) {
                    Files.createDirectories(pastaDestino);
                }

                String nomeArquivo = System.currentTimeMillis() + "_" + arquivoSelecionado.getName();
                Path caminhoDestino = pastaDestino.resolve(nomeArquivo);
                Files.copy(arquivoSelecionado.toPath(), caminhoDestino, StandardCopyOption.REPLACE_EXISTING);
                
                historicoDAO.adicionarImagem(idDoHistoricoParaTeste, caminhoDestino.toString());
                carregarImagensSalvas();
                JOptionPane.showMessageDialog(this, "Imagem adicionada com sucesso!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar a imagem.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Exibe a imagem no JLabel, redimensionando-a para caber no espaço
     * sem distorcer a proporção original.
     * @param arquivoImagem O arquivo de imagem a ser exibido.
     */
    private void exibirImagem(File arquivoImagem) {
        if (arquivoImagem != null && arquivoImagem.exists()) {
            ImageIcon iconeOriginal = new ImageIcon(arquivoImagem.getPath());
            Image imagemOriginal = iconeOriginal.getImage();

            int labelWidth = labelVisualizador.getWidth();
            int labelHeight = labelVisualizador.getHeight();

            // Se o label ainda não tiver tamanho, não faz nada.
            if (labelWidth <= 0 || labelHeight <= 0) {
                return;
            }

            int imgWidth = imagemOriginal.getWidth(null);
            int imgHeight = imagemOriginal.getHeight(null);

            // Calcula a proporção da imagem e do label
            double imgRatio = (double) imgWidth / (double) imgHeight;
            double labelRatio = (double) labelWidth / (double) labelHeight;
            
            int newWidth = imgWidth;
            int newHeight = imgHeight;

            // Se a imagem é mais larga (proporcionalmente) que o label,
            // a nova largura será a do label e a altura será calculada.
            if (imgRatio > labelRatio) {
                newWidth = labelWidth;
                newHeight = (int) (newWidth / imgRatio);
            } else { // Se a imagem é mais alta (proporcionalmente),
                     // a nova altura será a do label e a largura será calculada.
                newHeight = labelHeight;
                newWidth = (int) (newHeight * imgRatio);
            }

            // Cria a imagem redimensionada com alta qualidade
            Image imagemRedimensionada = imagemOriginal.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            
            labelVisualizador.setIcon(new ImageIcon(imagemRedimensionada));
            labelVisualizador.setText(null);
        } else {
            labelVisualizador.setIcon(null);
            labelVisualizador.setText("Imagem não encontrada");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CriadorTabelas.criarTabelas();
            new TesteAdicionarImagemGUI().setVisible(true);
        });
    }
}
