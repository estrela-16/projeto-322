package Principal;
public enum StatusPagamento {
    
    PAGA ("VERDE"),
    NAO_PAGA ("VERMELHO");

    private final String cor;

    StatusPagamento(String cor){
        this.cor = cor;
    }

    public String getCor(){
        return cor;
    }
}