package Principal;
public class Dentista extends Pessoa {

    private int id; // atributo para armazenar o ID do banco de dados
    private String cro;

    public Dentista(String nome, String cpf, String telefone, String cro) {
        super(nome, cpf, telefone);
        this.cro=cro;
    }

    public Dentista() { // Construtor vazio. Frequentemente usado por DAOs para criar um objeto e depois preencher seus atributos com os dados do ResultSet.
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

}
