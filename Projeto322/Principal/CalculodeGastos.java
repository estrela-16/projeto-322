package Principal;
import java.util.ArrayList;
import java.util.List;

public class CalculodeGastos {
    private List<MateriaisComuns> materialcomum;
    private int numerodeconsultas;
    private double luz;
    private double agua;
    private double aluguel;

    public CalculodeGastos(){
        this.materialcomum = new ArrayList<>();
    }

    public List<MateriaisComuns>getMateriaisComunses(){
        return materialcomum;
    }

    public double getAgua(){
        return agua;
    }

    public double getLuz(){
        return luz;
    }

    public double getAluguel(){
        return aluguel;
    }

    public double CalculodeMateriais(){
        double total = 0;
         for(int i = 0; i < materialcomum.size(); i++){
            total += (materialcomum.get(i).getQuantidade())*(materialcomum.get(i).getValor());
         }
         total = total/numerodeconsultas;
        return total;
    }

    public double Contas(double luz, double agua, double aluguel){
        double total = 0;
        total = (luz+agua+aluguel)/numerodeconsultas;
        return total;
    }

    public double gastosTotais(){
        double total = 0;
        double contaMatComum = CalculodeMateriais();
        double contas = Contas(luz, agua, aluguel);
        total = contaMatComum +contas;

        return total;
    }

}
