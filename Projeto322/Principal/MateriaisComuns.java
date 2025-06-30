// informacoes dos materiais comuns 

package Principal;

public class MateriaisComuns implements Id_Banco { // Implementa a interface
    private int id;
    private String nome;
    private double valor;
    private int quantidade;


    public MateriaisComuns(String nome, double valor, int quantidade){
        this.nome = nome;
        this.valor = valor;
        this.quantidade = quantidade;
    }

  
    public MateriaisComuns(int id, String nome, double valor, int quantidade){
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade(){
        return quantidade;
    }
    
  
    public String getNome() {
        return nome;
    }

    public double getValor() {
        return valor;
    }
}