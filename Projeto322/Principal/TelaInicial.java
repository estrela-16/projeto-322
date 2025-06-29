package Principal;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {
        super("Java Clinic");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Centraliza na tela

        PainelComImagemDeFundo painel = new PainelComImagemDeFundo("/imagens/painel.jpg");
        
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Color azulCaixas = new Color(70, 130, 180, 220); 

        // Título
        JLabel titulo = new JLabel("JAVA CLINIC");
        titulo.setFont(new Font("Serif", Font.BOLD, 55));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setForeground(Color.WHITE); 
        
        titulo.setBackground(azulCaixas);
        titulo.setOpaque(true);

        // --- LINHA DA BORDA DO TÍTULO MODIFICADA ---
        // Agora usa uma Borda Composta: uma linha branca por fora e um espaçamento por dentro.
        titulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),      // Borda externa (visível)
            BorderFactory.createEmptyBorder(10, 25, 10, 25) // Borda interna (padding)
        ));

        // (Opcional) logotipo
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setPreferredSize(new Dimension(300, 300));
        try {
            logoLabel.setIcon(new ImageIcon(getClass().getResource("/imagens/logo.png")));
        } catch (Exception e) {
            System.out.println("Arquivo 'logo.png' não encontrado. O logo não será exibido.");
        }

        // Botões
        JButton acessarButton = new JButton("Acessar Sistema");
        JButton sobreButton = new JButton("Sobre");
        JButton sairButton = new JButton("Sair");
        
        JButton[] botoes = {acessarButton, sobreButton, sairButton};
        Font fonteBotao = new Font("SansSerif", Font.BOLD, 14);

        for (JButton botao : botoes) {
            botao.setBackground(azulCaixas);
            botao.setForeground(Color.WHITE);
            botao.setFont(fonteBotao);
            botao.setAlignmentX(Component.CENTER_ALIGNMENT);
            botao.setMaximumSize(new Dimension(450, 50)); // Ajustei a altura para um valor mais usual
            botao.setFocusPainted(false);
            botao.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        }

        // Ações
        acessarButton.addActionListener(e -> {
            dispose(); 
            new ClinicaOdontoGUI(); 
        });

        sobreButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Sistema de Gerenciamento Odontológico\nVersão 1.0\nDesenvolvido em MC322",
                "Sobre", JOptionPane.INFORMATION_MESSAGE);
        });

        sairButton.addActionListener(e -> System.exit(0));

        // Adiciona os elementos ao painel
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaInicial::new);
    }
}