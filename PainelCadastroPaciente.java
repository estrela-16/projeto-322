import javax.swing.*;
import javax.swing.border.EmptyBorder; // Para adicionar espaçamento interno
import javax.swing.border.TitledBorder; // Para adicionar um título com borda
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PainelCadastroPaciente extends JPanel {

    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoTelefone;
    private JButton botaoSalvar;

    private List<Paciente> pacientesCadastrados; // Lista para simular o armazenamento

    public PainelCadastroPaciente() {
        pacientesCadastrados = new ArrayList<>();

        // 1. Layout Principal do Painel: BorderLayout
        setLayout(new BorderLayout(10, 10)); // Espaçamento de 10px entre as regiões
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Espaçamento interno (padding)

        // 2. Título da Tela
        JLabel tituloLabel = new JLabel("Cadastro de Novo Paciente", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Aumenta e negrita a fonte
        tituloLabel.setForeground(new Color(50, 100, 150)); // Cor azul escura
        add(tituloLabel, BorderLayout.NORTH); // Adiciona o título na parte superior

        // 3. Painel para o Formulário (Campos de Entrada)
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridBagLayout()); // Usando GridBagLayout para controle preciso
        painelFormulario.setBorder(new TitledBorder("Dados do Paciente")); // Borda com título

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Espaçamento entre os componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Preencher horizontalmente

        // Nome
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 0; // Linha 0
        gbc.anchor = GridBagConstraints.EAST; // Alinhar à direita
        painelFormulario.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1; // Coluna 1
        gbc.gridy = 0; // Linha 0
        gbc.weightx = 1.0; // Faz com que o campo se expanda horizontalmente
        campoNome = new JTextField(25); // Ajusta o tamanho preferencial
        painelFormulario.add(campoNome, gbc);

        // CPF
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 1; // Linha 1
        gbc.weightx = 0; // Reseta o peso
        painelFormulario.add(new JLabel("CPF:"), gbc);

        gbc.gridx = 1; // Coluna 1
        gbc.gridy = 1; // Linha 1
        gbc.weightx = 1.0;
        campoCpf = new JTextField(25);
        painelFormulario.add(campoCpf, gbc);

        // Telefone
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 2; // Linha 2
        gbc.weightx = 0;
        painelFormulario.add(new JLabel("Telefone:"), gbc);

        gbc.gridx = 1; // Coluna 1
        gbc.gridy = 2; // Linha 2
        gbc.weightx = 1.0;
        campoTelefone = new JTextField(25);
        painelFormulario.add(campoTelefone, gbc);

        // Adiciona o painel do formulário ao centro do PainelCadastroPaciente
        add(painelFormulario, BorderLayout.CENTER);

        // 4. Painel para o Botão
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15)); // Centraliza o botão, espaçamento vertical
        botaoSalvar = new JButton("Salvar Paciente");
        botaoSalvar.setFont(new Font("Arial", Font.BOLD, 16)); // Aumenta a fonte do botão
        botaoSalvar.setBackground(new Color(70, 130, 180)); // Cor de fundo azul aço
        botaoSalvar.setForeground(Color.WHITE); // Cor do texto branca
        botaoSalvar.setFocusPainted(false); // Remove o contorno de foco
        painelBotoes.add(botaoSalvar);

        add(painelBotoes, BorderLayout.SOUTH); // Adiciona o painel de botões na parte inferior

        // Adiciona o listener ao botão Salvar
        botaoSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarPaciente();
            }
        });
    }

    private void salvarPaciente() {
        String nome = campoNome.getText().trim(); // Remove espaços em branco extras
        String cpf = campoCpf.getText().trim();
        String telefone = campoTelefone.getText().trim();

        // Validação mais robusta (ex: verificar formato do CPF, etc.)
        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos os campos (Nome, CPF, Telefone) são obrigatórios.",
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Você pode adicionar validações de formato aqui, por exemplo, para CPF e telefone
        if (!cpf.matches("\\d{11}")) { // Exemplo: verifica se tem 11 dígitos
            JOptionPane.showMessageDialog(this,
                    "O CPF deve conter 11 dígitos numéricos.",
                    "Erro de Formato",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cria um novo objeto Paciente
        Paciente novoPaciente = new Paciente(nome, cpf, telefone);
        pacientesCadastrados.add(novoPaciente); // Adiciona à lista simulada

        // Exibe mensagem de sucesso
        JOptionPane.showMessageDialog(this,
                "Paciente '" + nome + "' cadastrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

        // Limpa os campos após o cadastro
        campoNome.setText("");
        campoCpf.setText("");
        campoTelefone.setText("");

        // Para fins de demonstração, imprime no console
        System.out.println("--- Pacientes Cadastrados ---");
        for (Paciente p : pacientesCadastrados) {
            System.out.println("Nome: " + p.getNome() + ", CPF: " + p.getCpf() + ", Telefone: " + p.getTelefone());
        }
        System.out.println("-----------------------------");
    }

    public List<Paciente> getPacientesCadastrados() {
        return pacientesCadastrados;
    }
}