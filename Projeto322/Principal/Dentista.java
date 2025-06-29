package Principal;
public class Dentista extends Pessoa {

    private int id; // atributo para armazenar o ID do banco de dados
    private String cro;

    public Dentista(String nome, String cpf, String telefone, String cro) {
        super(nome, cpf, telefone);
        this.id=0;//valor arbitrário pois o Id e definido apenas após adicionar no banoc de dados
        this.cro=cro;
    }

    // Construtor para RECUPERAR um Dentista do banco de dados (com ID já definido)
    public Dentista(int id, String nome, String cpf, String telefone, String cro) {
        super(nome, cpf, telefone);
        this.id = id;
        this.cro = cro;
    }

    public Dentista() { // Construtor vazio. Frequentemente usado por DAOs para criar um objeto e depois preencher seus atributos com os dados do ResultSet.
        super();
        this.id=0;
    }

    @Override
    public String toString() {
        return getNome() + " - CRO: " + cro;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {//utilizado depois de adicionar o dentista na tabela para definir o seu id
        this.id = id;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

}
