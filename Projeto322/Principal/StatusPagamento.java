package Principal;
public enum StatusPagamento { // indica o status da consulta
    
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
