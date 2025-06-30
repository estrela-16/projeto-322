package Principal;
// informacoes do dentista 
public class Dentista extends Pessoa implements Id_Banco {

    private int id; // atributo para armazenar o ID do banco de dados
    private String cro;

    public Dentista(String nome, String cpf, String telefone, String cro) {
        super(nome, cpf, telefone);
        this.id=0;
        this.cro=cro;
    }

    
    public Dentista(int id, String nome, String cpf, String telefone, String cro) {
        super(nome, cpf, telefone);
        this.id = id;
        this.cro = cro;
    }

    public Dentista() { 
        super();
        this.id=0;
    }

    @Override
    public String toString() {
        return getNome() + " - CRO: " + cro;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
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
