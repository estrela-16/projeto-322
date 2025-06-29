package Principal;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o histórico de um paciente.
 * Contém a descrição textual dos procedimentos passados e uma lista de caminhos
 * para arquivos de imagem relevantes (como raios-x).
 */
public class Historico implements Id_Banco{

    private int id; // ID do histórico no banco de dados
    private int pacienteId; // ID do paciente ao qual este histórico pertence
    private String descricao; // Conteúdo do arquivo de texto com problemas passados, anotações, etc.
    private List<String> caminhosImagens; // Lista de caminhos para os arquivos de imagem

    //Inicializa a lista de imagens. 
    public Historico() {
        this.caminhosImagens = new ArrayList<>();
    }

    /**
     * Construtor para criar um novo histórico antes de salvar no banco de dados.
     * pacienteId O ID do paciente.
     * descricao A descrição textual do histórico.
     */
    public Historico(int pacienteId, String descricao) {
        this.pacienteId = pacienteId;
        this.descricao = descricao;
        this.caminhosImagens = new ArrayList<>();
    }

    // --- Getters e Setters ---

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getCaminhosImagens() {
        return caminhosImagens;
    }

    public void setCaminhosImagens(List<String> caminhosImagens) {
        this.caminhosImagens = caminhosImagens;
    }

    // --- Métodos de ajuda ---

    /**
     * Adiciona o caminho de uma nova imagem à lista de imagens do histórico.
     * caminho O caminho do arquivo de imagem.
     */
    public void adicionarImagem(String caminho) {
        if (this.caminhosImagens == null) {
            this.caminhosImagens = new ArrayList<>();
        }
        this.caminhosImagens.add(caminho);
    }
}