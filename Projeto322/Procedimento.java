import java.util.ArrayList;
import java.util.List;


public class Procedimento {
   private String nome;
   private List<Materiais> material;


   public Procedimento(String nome) {
       this.nome = nome;
       this.material = new ArrayList<>(); /* lista de objetos Materiais */
   }


   public String getNome(){
       return nome;
   }


   public List<Materiais>getMateriais(){
       return material;
   }


   public void adicionarMaterial (Materiais objeto){
       this.material.add(objeto);
   }


   public void removerMaterial (Materiais objeto){
       this.material.remove(objeto);
   }


    public double calcularValor(){
       double total = 0;
       for(int i = 0; i <  material.size(); i++){
           total += material.get(i).getValor(); /* pega o Material na lista e dele pega o valor */


       }
       return total;
   }





}
