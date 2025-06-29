package Principal;
import java.time.LocalDate;


public class Atendimento {
   private LocalDate data;
   private Procedimento procedimento;
   private Paciente paciente;
   private String horario;
   private Dentista dentista;
   private StatusConsulta statusC;


    public Atendimento(LocalDate data, String horario, Procedimento procedimento, Paciente paciente,
                   Dentista dentista, StatusConsulta statusC) {
        this.data = data;
        this.horario = horario;
        this.procedimento = procedimento;
        this.paciente = paciente;
        this.dentista = dentista;
        this.statusC = statusC;
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

}
