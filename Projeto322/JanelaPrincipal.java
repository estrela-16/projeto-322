import javax.swing.*;
import java.awt.*; // Importa classes relacionadas a gráficos e UI

public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal() {
        super("Sistema de Gerenciamento Odontológico");

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        // Cria uma aba para o cadastro de pacientes
        JTabbedPane abas = new JTabbedPane();

        // Instancia o painel de cadastro de pacientes
        PainelCadastroPaciente painelCadastroPaciente = new PainelCadastroPaciente();

        // Adiciona o painel de cadastro de pacientes a uma aba
        abas.addTab("Cadastro de Pacientes", painelCadastroPaciente);

        // Adiciona um painel de "Boas-vindas" para ter outra aba
        JPanel painelBoasVindas = new JPanel();
        painelBoasVindas.setLayout(new FlowLayout());
        JLabel labelBoasVindas = new JLabel("Bem-vindo ao Sistema Odontológico!");
        labelBoasVindas.setFont(new Font("Arial", Font.BOLD, 24));
        painelBoasVindas.add(labelBoasVindas);
        abas.addTab("Início", painelBoasVindas);


        // Adiciona o JTabbedPane à janela principal
        add(abas, BorderLayout.CENTER);
    }
}