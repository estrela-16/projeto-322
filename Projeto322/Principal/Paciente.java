package Principal;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Pessoa {
    private Historico historico;
    private int id;

    public Paciente(String nome, String cpf, String telefone) {
        super(nome, cpf, telefone);
        this.historico = new Historico();
    }

    //Construtor para RECUPERAR um Paciente do banco de dados (com ID já definido)
    public Paciente(int id,String nome, String cpf, String telefone) {
        super(nome, cpf, telefone);
        this.historico = new Historico();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {//utilizado depois de adicionar o dentista na tabela para definir o seu id
        this.id = id;
    }
    
    public Historico getHistorico() {
        return historico;
    }

    public void setHistoricoDe(Historico historico) {
        this.historico = historico;
    }

    
}
