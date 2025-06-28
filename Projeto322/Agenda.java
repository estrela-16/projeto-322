import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class Agenda {
   private Map <LocalDate, Atendimento > atendimentos;
  
   public Agenda(){
       atendimentos= new HashMap<>();
   }


   public void agendar (LocalDate data, Procedimento procedimento){
       Atendimento a = atendimentos.getOrDefault(data, new Atendimento(data));
       atendimentos.put(data,a);
   }
  
   public Atendimento getAtendimentoDia (LocalDate data){
       return  atendimentos.get(data);
   }  


   public Map<LocalDate, Atendimento> getTodosAtendimentos(){
       return atendimentos;
   }


}






