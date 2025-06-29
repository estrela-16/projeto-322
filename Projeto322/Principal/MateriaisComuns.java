package Principal;
public class MateriaisComuns extends Materiais {
    private int quantidade;

    public MateriaisComuns(String nome, double valor, int quantidade){
        super(nome,valor);
        this.quantidade = quantidade;
    }

    public int getQuantidade(){
        return quantidade;
    }

}
