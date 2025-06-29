package Principal;
public class Dentista extends Pessoa {

    private String cro;

    public Dentista(String nome, String cpf, String telefone) {
        super(nome, cpf, telefone);
    }

    String getCro(){
        return this.cro;
    }

}
