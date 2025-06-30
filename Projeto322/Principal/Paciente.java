package Principal;

import DAO.HistoricoDAO; 

/**
 * Representa um Paciente.
 
 */
public class Paciente extends Pessoa implements Id_Banco{
    private int id;
    private Historico historico;

    // Construtor para criar um novo paciente antes de salvar no banco
    public Paciente(String nome, String cpf, String telefone) {
        super(nome, cpf, telefone);
        this.historico = null;
    }

  
    public Paciente(int id, String nome, String cpf, String telefone) {
        super(nome, cpf, telefone);
        this.id = id;
    }

    
    public Paciente() {
        super();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getNome();
    }
    
    public Historico getHistorico() {
        return historico;
    }

    /**
     * Define o objeto de histórico para este paciente.
     * Isso é feito após carregar o paciente e o histórico separadamente do banco.
     * @param historico O objeto de histórico carregado.
     */
    public void setHistorico(Historico historico) {
        this.historico = historico;
    }
    
   
    /**
     * Modifica a descrição do histórico do paciente e persiste a mudança no banco de dados.
     * Este método só deve ser chamado depois que o histórico do paciente for carregado.
     *
     * @param novaDescricao A nova descrição textual para o histórico.
     * @param dao A instância do HistoricoDAO para realizar a atualização no banco.
     */
    public void definirDescricaoHistorico(String novaDescricao, HistoricoDAO dao) {
        if (this.historico == null) {
            System.err.println("Erro: O histórico deste paciente ainda não foi carregado ou criado.");
            return;
        }
      
        this.historico.setDescricao(novaDescricao);
        
        
        dao.atualizar(this.historico);
    }

    /**
     * Adiciona uma nova imagem ao histórico do paciente e persiste a mudança no banco.
     * Este método só deve ser chamado depois que o histórico do paciente for carregado.
     * * @param caminhoImagem O caminho do arquivo de imagem a ser adicionado.
     * @param dao A instância do HistoricoDAO para realizar a inserção no banco.
     */
    public void adicionarImagemAoHistorico(String caminhoImagem, HistoricoDAO dao) {
        if (this.historico == null) {
            System.err.println("Erro: O histórico deste paciente ainda não foi carregado ou criado.");
            return;
        }
        
        this.historico.adicionarImagem(caminhoImagem);

      
        dao.adicionarImagem(this.historico.getId(), caminhoImagem);
    }
}
