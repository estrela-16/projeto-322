public class MateriaisComuns extends Materiais {
    private int quantidade;

    public MateriaisComuns(String nome, double valor){
        super(nome,valor);
        this.quantidade = quantidade;
    }

    public int getQuantidade(){
        return quantidade;
    }

}
