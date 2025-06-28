package Principal;
import java.time.LocalDate;

import StatusConsulta;
import StatusPagamento;


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
  



   public double valorFinal ( CalculodeGastos calculadora){
       double valorFixo = calculadora.gastosTotais();
       double valorEquipamentos = procedimento.calcularValor();
       return valorFixo + valorEquipamentos;
       
   }


}
