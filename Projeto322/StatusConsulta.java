public enum StatusConsulta {
    
    PAGA("Verde"),
    NAO_PAGA("Vermelha");

    private final String cor;

    StatusConsulta(String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }
}
