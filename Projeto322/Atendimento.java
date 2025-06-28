import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Atendimento {
   private LocalDate data;
   private List <Procedimento> procedimentos;
   private String paciente;


   public Atendimento (LocalDate data){
       this.data = data;
       this.procedimentos  = new ArrayList<>();
       this.paciente = paciente;
   }


   public LocalDate getData(){
       return data;
   }


   public List<Procedimento> getProcedimentos() {
       return procedimentos;
   }


   public String getPaciente(){
       return paciente;
  }
   public void adicionarProcedimento(Procedimento p) {
       procedimentos.add(p);
   }




   public void removerProcedimento(Procedimento p) {
       procedimentos.remove(p);
   }


   public double valorFinal ( Procedimento procedimentos){
       double valorFixo = 0;
       double valorEquipamentos = procedimentos.calcularValor();
       double valorFInal = valorFixo + valorEquipamentos;
       return valorFInal;
   }


}
