package Principal;
import java.util.ArrayList;
import java.util.List;

public class Procedimento {
    private String nome;
    private String especialidade;
    private List<Materiais> materiais;
    private CalculodeGastos gastos;
    private int id;
    private double preco;

    public Procedimento(String nome, String especialidade) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.materiais = new ArrayList<>();
    }

    // Construtor para recuperar do banco de dados
    public Procedimento(int id, String nome, String especialidade, double preco) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.preco=preco;
        this.materiais = new ArrayList<>();
    }

    // Construtor vazio para atualizações
    public Procedimento() {
        this.materiais = new ArrayList<>();
    }


    public String getNome() {
        return nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public List<Materiais> getMateriais() {
        return materiais;
    }

    public double getPreco(){
        this.preco=this.calcularCustoTotal();
        return this.calcularCustoTotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void adicionarMaterial(Materiais m) {
        materiais.add(m);
    }

    public void removerMaterial(Materiais m) {
        materiais.remove(m);
    }

    public double calcularCustoTotal() {
        double total = 0;
        for (Materiais m : materiais) {
            total += m.getValor();
        }
        double taxa;
        taxa = gastos.getTaxaServico();
        total = taxa*(total + gastos.gastosTotais());

        return total;
    }

    @Override
    public String toString() {
        return nome + " (" + especialidade + ") - Custo total: R$" + String.format("%.2f", calcularCustoTotal());
    }
}