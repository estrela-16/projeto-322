import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Atendimento {

    private LocalDate data;
    private List<Procedimento> procedimentos;
    private Paciente paciente;
    private StatusConsulta statusC;
    private StatusPagamento statusP;

    public Atendimento(Paciente paciente, LocalDate data, StatusConsulta statusC, StatusPagamento statusP) {
        this.data = data;
        this.paciente = paciente;
        this.statusC = statusC;
        this.statusP = statusP;
        this.procedimentos = new ArrayList<>();
    }

    public LocalDate getData() {
        return data;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public StatusConsulta getStatusConsulta() {
        return statusC;
    }

    public StatusPagamento getStatusPagamento() {
        return statusP;
    }

    public List<Procedimento> getProcedimentos() {
        return procedimentos;
    }

    public void adicionarProcedimento(Procedimento p) {
        procedimentos.add(p);
    }

    public void removerProcedimento(Procedimento p) {
        procedimentos.remove(p);
    }

    public void moudarEstadoConsulta(Procedimento p){


    }

    public void mudarEStadoPagamento(Procedimento p){
    
        
    }

    public double valorEquipamentosFinal() {
    double total = 0;
    for (Procedimento p : procedimentos) {
        total += p.calcularCustoTotal(); 
    }
    return total;
}

    public double valorFinal() {
        double valorFixo = 0; 
        return valorFixo + valorEquipamentosFinal();
    }

}