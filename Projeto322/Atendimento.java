import java.time.LocalDate;


public class Atendimento {
   private LocalDate data;
   private Procedimento procedimento;
   private Paciente paciente;


   public Atendimento (LocalDate data){
       this.data = data;
       this.procedimento  = procedimento;
       this.paciente = paciente;
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

   public double valorFinal ( Procedimento procedimentos){
       double valorFixo = 0;
       double valorEquipamentos = procedimentos.calcularValor();
       double valorFInal = valorFixo + valorEquipamentos;
       return valorFInal;
   }


}
