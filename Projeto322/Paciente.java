import java.util.ArrayList;
import java.util.List;

public class Paciente extends Pessoa {
    private List<Procedimento> historico;

    public Paciente(String nome, String cpf, String telefone) {
        super(nome, cpf, telefone);
        this.historico = new ArrayList<>();
    }

    public void adicionarProcedimento(Procedimento procedimento) {
        historico.add(procedimento);
    }

    public List<Procedimento> getHistorico() {
        return historico;
    }

    
}
