package Principal;
import java.time.LocalDate;


public class Atendimento {
   private LocalDate data;
   private Procedimento procedimento;
   private Paciente paciente;
   private String horario;
   private Dentista dentista;
   private StatusPagamento statusP;


    public Atendimento(LocalDate data, String horario, Procedimento procedimento, Paciente paciente, Dentista dentista, StatusPagamento statusP) {
        this.data = data;
        this.horario = horario;
        this.procedimento = procedimento;
        this.paciente = paciente;
        this.dentista = dentista;
        this.statusP = statusP;
    }

   public LocalDate getData(){
       return data;
   }


   public Procedimento getProcedimentos() {
       return procedimento;
   }


   public Paciente getPaciente(){
       return paciente;
  }
  

    public String getHorario() {
    return horario;
    }

    public Dentista getDentista() {
        return dentista;
    }

   public double getCustoMateriais(){ /*custo dos materiais + manutencao do consultorio */
        return procedimento.calcularCustoTotal();
   }

   public void mudarStatusDePagamento(){
    if (statusP != null){
        if (statusP == StatusPagamento.PAGO){
            statusP = StatusPagamento.NAO_PAGO;
        } else {
            statusP = StatusPagamento.PAGO;
        }

    }
   }

}
