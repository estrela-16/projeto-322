public enum StatusConsulta {
    
    AGENDADA("Verde"),
    REALIZADA("Vermelha"),
    CANCELADA("Cinza");
    
    private final String cor;

    StatusConsulta(String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }
}
