package Principal;
import java.util.ArrayList;
import java.util.List;

public class Procedimento {
    private int id;
    private String nome;
    private String especialidade;
    private List<Materiais> materiais;
    private CalculodeGastos gastos;
    private double preco;

    public Procedimento(String nome, String especialidade, CalculodeGastos gastos) {
        this.id=0;
        this.nome = nome;
        this.especialidade = especialidade;
        this.gastos=gastos;
        this.materiais = new ArrayList<>();
        this.preco=calcularCustoTotal();
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
        this.preco= this.calcularCustoTotal();
    }

    public void removerMaterial(Materiais m) {
        materiais.remove(m);
        this.preco=this.calcularCustoTotal();
    }

    public double calcularGastos(){
        double total = 0;
        for (Materiais m : materiais) {
            total += m.getValor();
        }
        return total+gastos.gastosTotais();
    }

    public double calcularCustoTotal() {
        double taxa;
        taxa = gastos.getTaxaServico();
        double gastos=calcularGastos();
        double total = taxa*(gastos);

        return total;
    }

    @Override
    public String toString() {
        return nome + " (" + especialidade + ") - Custo total: R$" + String.format("%.2f", calcularCustoTotal());
    }
}