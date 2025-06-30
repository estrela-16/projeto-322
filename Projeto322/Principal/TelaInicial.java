package Principal;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {
        super("Java Clinic");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Centraliza na tela

        // Usa o painel customizado com a imagem de fundo
        PainelComImagemDeFundo painel = new PainelComImagemDeFundo("/imagens/painel.jpg");
        
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Cor base para os elementos da interface
        Color azulCaixas = new Color(70, 130, 180, 220); 

        // Título estilizado com uma caixa de fundo
        JLabel titulo = new JLabel("JAVA CLINIC");
        titulo.setFont(new Font("Serif", Font.BOLD, 55));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setForeground(Color.WHITE); 
        titulo.setBackground(azulCaixas);
        titulo.setOpaque(true);
        titulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),      // Borda externa visível
            BorderFactory.createEmptyBorder(10, 25, 10, 25) // Borda interna para padding
        ));

        // Logotipo
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setPreferredSize(new Dimension(300, 300));
        try {
            logoLabel.setIcon(new ImageIcon(getClass().getResource("/imagens/logo.png")));
        } catch (Exception e) {
            System.out.println("Arquivo 'logo.png' não encontrado. O logo não será exibido.");
        }

        // Botões de navegação
        JButton acessarButton = new JButton("Acessar Sistema");
        JButton sobreButton = new JButton("Sobre");
        JButton sairButton = new JButton("Sair");
        
        // Aplicação de estilo comum a todos os botões
        JButton[] botoes = {acessarButton, sobreButton, sairButton};
        Font fonteBotao = new Font("SansSerif", Font.BOLD, 14);

        for (JButton botao : botoes) {
            botao.setBackground(azulCaixas);
            botao.setForeground(Color.WHITE);
            botao.setFont(fonteBotao);
            botao.setAlignmentX(Component.CENTER_ALIGNMENT);
            botao.setMaximumSize(new Dimension(450, 50));
            botao.setFocusPainted(false);
            botao.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        }

        // Ações dos botões
        acessarButton.addActionListener(e -> {
            dispose(); 
            new ClinicaOdontoGUI(); 
        });

        sobreButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Sistema de Gerenciamento Odontológico\nVersão 1.0\nDesenvolvido por Ana Carolina Vieira Araujo, Maria Leticia Gomes Braga dos Reis, Júlia de Souza Nardo, Maria Clara Martinez de Oliveira",
                "Sobre", JOptionPane.INFORMATION_MESSAGE);
        });

        sairButton.addActionListener(e -> System.exit(0));

        // Adiciona os elementos ao painel principal com espaçamento
        painel.add(Box.createVerticalGlue()); 
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(20));
        painel.add(logoLabel);
        painel.add(Box.createVerticalStrut(20));
        painel.add(acessarButton);
        painel.add(Box.createVerticalStrut(10));
        painel.add(sobreButton);
        painel.add(Box.createVerticalStrut(10));
        painel.add(sairButton);
        painel.add(Box.createVerticalGlue()); 

        add(painel);
        setVisible(true);
    }

    /**
     * Ponto de entrada da aplicação.
     * Configura o Look and Feel moderno (FlatLaf) antes de criar a janela.
     */
    public static void main(String[] args) {
        // --- ATIVAÇÃO DO LOOK AND FEEL MODERNO ---
        try {
            // Define o tema FlatLaf Light. Esta linha transforma a aparência de toda a aplicação.
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar o tema Look and Feel.");
        }
        // --- FIM DA ATIVAÇÃO ---

        // Garante que a criação da interface gráfica ocorra na thread correta (Event Dispatch Thread)
        SwingUtilities.invokeLater(TelaInicial::new);
    }
}