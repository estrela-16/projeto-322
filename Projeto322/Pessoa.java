public class Pessoa {
  
    private String nome;
    private int cpf;
    private int telefone;

    public Pessoa(String nome, int cpf, int telefone){
        nome = this.nome;
        cpf = this.cpf;
        telefone = this.telefone;

    }

    public String getNome() {
        return nome;
    }

    public int getCpf() {
        return cpf;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(int cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(int numero_telefone) {
        this.telefone = numero_telefone;
    }

}
