package Principal;

public class Contas implements Id_Banco{
    private int id;
    private String nome;
    private double valor;

    public Contas (String nome, double valor){
        this.nome = nome;
        this.valor = valor;
    }

    // Construtor para buscar do BD (com id)
    public Contas (int id, String nome, double valor){
        this.id = id;
        this.nome = nome;
        this.valor = valor;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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