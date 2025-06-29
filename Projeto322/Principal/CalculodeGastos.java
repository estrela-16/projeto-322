package Principal;
import java.util.ArrayList;
import java.util.List;

public class CalculodeGastos {
    private List<MateriaisComuns> materialcomum;
    private int numerodeconsultas;
    private List<Contas>conta;
    private double comissao;
    private double servico;
<<<<<<< HEAD
=======

>>>>>>> refs/remotes/origin/main


    public CalculodeGastos(){
        this.materialcomum = new ArrayList<>();
        this.conta = new ArrayList<>();
    }

    public List<MateriaisComuns>getMateriaisComunses(){
        return materialcomum;
    }

    public double getComissao(){
        return comissao;
    }

    public List<Contas> getConta(){
        return conta;
    }

    public double getTaxaServico(){
        return servico;
    }

    public double CalculodeMateriais(){
        double total = 0;
         for(int i = 0; i < materialcomum.size(); i++){
            total += (materialcomum.get(i).getQuantidade())*(materialcomum.get(i).getValor());
         }
         total = total/numerodeconsultas;
        return total;
    } 
    public void setNumeroDeConsultas(int num) {
        this.numerodeconsultas = num;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }
    
       public void setTaxaServico(double servico) {
        this.servico = servico;
    }

    public double Contas(){
        double total = 0;
        for(int i = 0; i < conta.size(); i++){
            total += conta.get(i).getValor();
        }
        total = total/numerodeconsultas;
        return total;
    }

    public double gastosTotais(){
        double total = 0;
        double contaMatComum = CalculodeMateriais();
        double contas = Contas();
        total = contaMatComum +contas;

        return total;
    }

    public void setNumeroDeConsultas(int num){
        this.numerodeconsultas = num;
    }

    public void setComissao(double comissao){
        this.comissao = comissao;

    }

    public void setTaxaServico(double servico){
        this.servico = servico;
    }

}
