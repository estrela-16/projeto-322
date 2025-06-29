package Principal;
public enum StatusPagamento {
    
    PAGO("Verde"),
    NAO_PAGO("Vermelha");
    
    private final String cor;

    StatusPagamento(String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }
}
