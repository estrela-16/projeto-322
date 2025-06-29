package Principal;

public class Contas {
    private String nome;
    private double valor;

    public Contas (String nome, double valor){
        this.nome = nome;
        this.valor = valor;
    }
 

    public String getNome(){
        return nome;
    }

    public double getValor(){
        return valor;
    }

    public void setValor(double valor){
        this.valor = valor;
    }

}