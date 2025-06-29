package Principal;
import java.time.LocalDate;


public class Atendimento {
   private LocalDate data;
   private Procedimento procedimento;
   private Paciente paciente;
   private StatusConsulta statusC;
   private StatusPagamento statusP;


   public Atendimento(LocalDate data, Procedimento procedimento, Paciente paciente,
                   StatusConsulta statusC, StatusPagamento statusP){
       this.data = data;
       this.procedimento  = procedimento;
       this.paciente = paciente;
       this.statusC=statusC;
       this.statusP=statusP;
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
  

   public double getCustoMateriais(){ /*custo dos materiais + manutencao do consultorio */
        return procedimento.calcularCustoTotal();
   }

   public double valorFinal ( CalculodeGastos calculadora){
       double valorFixo = calculadora.gastosTotais();
       double valorEquipamentos = getCustoMateriais();
       return valorFixo + valorEquipamentos;
       
   }
}
