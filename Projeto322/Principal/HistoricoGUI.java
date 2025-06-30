package Principal;

import DAO.HistoricoDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Interface gráfica para visualizar e gerenciar o histórico de um paciente,
 * incluindo uma descrição textual e uma lista de imagens.
 */
public class HistoricoGUI extends JFrame {

    // --- Componentes da Interface ---
    private JTextArea areaDescricao;
    private JButton botaoSalvarDescricao;
    private JButton botaoAdicionarImagem;
    private JLabel labelVisualizador;
    private JList<String> listaCaminhosImagens;
    private DefaultListModel<String> listModel;
    private JSplitPane splitPane;

    // --- Lógica de Dados ---
    private Paciente paciente;
    private Historico historico;
    private HistoricoDAO historicoDAO;

    public HistoricoGUI(Paciente paciente) {
        super("Histórico do Paciente: " + paciente.getNome());
        this.paciente = paciente;
        this.historicoDAO = new HistoricoDAO();

        // Tenta carregar o histórico existente ou cria um novo se não existir.
        carregarOuCriarHistorico();

        configurarJanela();
        inicializarComponentes();
        organizarLayout();
        definirAcoes();

        carregarDadosDoHistorico();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setSize(900, 700);
        setLocationRelativeTo(null); // Centraliza na tela
    }

    /**
     * Carrega o histórico do paciente do banco de dados. Se não existir,
     * cria um novo objeto Historico associado a este paciente.
     */
    private void carregarOuCriarHistorico() {
        this.historico = historicoDAO.buscarPorPacienteId(paciente.getId());
        if (this.historico == null) {
            // Se não existe histórico no banco, cria um novo objeto em memória
            this.historico = new Historico(paciente.getId(), "Nenhuma descrição ainda.");
            // Não insere no banco ainda. A inserção ocorrerá ao salvar a primeira descrição.
            System.out.println("Criando novo objeto de histórico para o paciente ID: " + paciente.getId());
        }
    }

    private void inicializarComponentes() {
        // --- Painel de Descrição (Esquerda) ---
        areaDescricao = new JTextArea();
        areaDescricao.setWrapStyleWord(true);
        areaDescricao.setLineWrap(true);
        areaDescricao.setFont(new Font("SansSerif", Font.PLAIN, 14));
        botaoSalvarDescricao = new JButton("Salvar Descrição");

        // --- Painel de Imagens (Direita) ---
        labelVisualizador = new JLabel("Selecione uma imagem da lista", SwingConstants.CENTER);
        labelVisualizador.setBorder(BorderFactory.createTitledBorder("Visualizador"));

        listModel = new DefaultListModel<>();
        listaCaminhosImagens = new JList<>(listModel);
        botaoAdicionarImagem = new JButton("Adicionar Nova Imagem");

        // --- Painel Divisor ---
        // Cria os painéis que ficarão dentro do JSplitPane
        JPanel painelEsquerdo = new JPanel(new BorderLayout(5, 5));
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Descrição do Histórico"));
        painelEsquerdo.add(new JScrollPane(areaDescricao), BorderLayout.CENTER);
        painelEsquerdo.add(botaoSalvarDescricao, BorderLayout.SOUTH);

        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Imagens do Histórico"));
        JScrollPane listaScrollPane = new JScrollPane(listaCaminhosImagens);
        listaScrollPane.setPreferredSize(new Dimension(200, 0)); // Largura preferencial para a lista
        painelDireito.add(listaScrollPane, BorderLayout.WEST);
        painelDireito.add(new JScrollPane(labelVisualizador), BorderLayout.CENTER);
        painelDireito.add(botaoAdicionarImagem, BorderLayout.SOUTH);

        // O JSplitPane divide a tela em duas
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setDividerLocation(300); // Posição inicial do divisor
    }
    
    private void organizarLayout() {
        // Adiciona o painel divisor ao frame
        add(splitPane, BorderLayout.CENTER);
    }

    private void definirAcoes() {
        botaoSalvarDescricao.addActionListener(e -> salvarDescricao());
        botaoAdicionarImagem.addActionListener(e -> adicionarNovaImagem());

        listaCaminhosImagens.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String caminhoSelecionado = listaCaminhosImagens.getSelectedValue();
                if (caminhoSelecionado != null) {
                    exibirImagem(new File(caminhoSelecionado));
                }
            }
        });
    }

    /**
     * Preenche a interface com os dados carregados do objeto Histórico.
     */
    private void carregarDadosDoHistorico() {
        // Carrega a descrição
        areaDescricao.setText(historico.getDescricao());

        // Carrega a lista de imagens
        listModel.clear();
        List<String> caminhos = historico.getCaminhosImagens();
        if (caminhos.isEmpty()) {
            listModel.addElement("Nenhuma imagem salva.");
        } else {
            for (String caminho : caminhos) {
                listModel.addElement(caminho);
            }
        }
    }

    private void salvarDescricao() {
        // Se o histórico ainda não foi salvo no banco (não tem ID), insira-o primeiro.
        if (historico.getId() == 0) {
            boolean sucesso = historicoDAO.inserir(historico);
            if (!sucesso) {
                JOptionPane.showMessageDialog(this, "Falha ao criar o registro de histórico inicial.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Atualiza o objeto em memória e depois no banco
        historico.setDescricao(areaDescricao.getText());
        historicoDAO.atualizar(historico);
        JOptionPane.showMessageDialog(this, "Descrição salva com sucesso!");
    }

    private void adicionarNovaImagem() {
        // Garante que o histórico já exista no banco antes de adicionar uma imagem
        if (historico.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Você precisa salvar uma descrição antes de adicionar imagens.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Selecione uma imagem");
        seletor.setFileFilter(new FileNameExtensionFilter("Imagens", "jpg", "png", "gif", "bmp"));

        if (seletor.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = seletor.getSelectedFile();
            try {
                // Cria uma pasta para armazenar as imagens, caso não exista
                Path pastaDestino = Paths.get("imagens_historico");
                if (!Files.exists(pastaDestino)) {
                    Files.createDirectories(pastaDestino);
                }

                // Cria um nome de arquivo único para evitar sobreposições
                String nomeArquivo = System.currentTimeMillis() + "_" + arquivoSelecionado.getName();
                Path caminhoDestino = pastaDestino.resolve(nomeArquivo);

                // Copia o arquivo para a pasta do projeto
                Files.copy(arquivoSelecionado.toPath(), caminhoDestino, StandardCopyOption.REPLACE_EXISTING);
                
                // Adiciona o caminho da imagem no banco de dados e atualiza a lista
                historicoDAO.adicionarImagem(historico.getId(), caminhoDestino.toString());
                
                // Recarrega a lista de imagens do banco
                historico.setCaminhosImagens(historicoDAO.buscarImagens(historico.getId()));
                carregarDadosDoHistorico();

                JOptionPane.showMessageDialog(this, "Imagem adicionada com sucesso!");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar a imagem: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Exibe a imagem no JLabel, redimensionando-a para caber no espaço.
     * (Este código é adaptado do seu TesteAdicionarImagemGUI.java)
     */
    private void exibirImagem(File arquivoImagem) {
        if (arquivoImagem != null && arquivoImagem.exists()) {
            ImageIcon iconeOriginal = new ImageIcon(arquivoImagem.getPath());
            Image imagemOriginal = iconeOriginal.getImage();

            int labelWidth = labelVisualizador.getWidth();
            int labelHeight = labelVisualizador.getHeight();

            if (labelWidth <= 0 || labelHeight <= 0) return;

            double imgRatio = (double) imagemOriginal.getWidth(null) / imagemOriginal.getHeight(null);
            double labelRatio = (double) labelWidth / labelHeight;
            
            int newWidth = (imgRatio > labelRatio) ? labelWidth : (int) (labelHeight * imgRatio);
            int newHeight = (imgRatio > labelRatio) ? (int) (labelWidth / imgRatio) : labelHeight;

            Image imagemRedimensionada = imagemOriginal.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            
            labelVisualizador.setIcon(new ImageIcon(imagemRedimensionada));
            labelVisualizador.setText(null);
        } else {
            labelVisualizador.setIcon(null);
            labelVisualizador.setText("Imagem não encontrada");
        }
    }
}