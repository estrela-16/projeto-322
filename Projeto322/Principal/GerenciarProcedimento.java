package Principal;
import java.util.ArrayList;
import java.util.List;

public class GerenciarProcedimento {
    private List<Procedimento> procedimentos;

    public GerenciarProcedimento() {
        this.procedimentos = new ArrayList<>();
    }

    public void adicionarProcedimento(Procedimento p) {
        procedimentos.add(p);
    }

    public void removerProcedimento(Procedimento p) {
        procedimentos.remove(p);
    }

    public List<Procedimento> getProcedimentos() {
        return procedimentos;
    }
}

