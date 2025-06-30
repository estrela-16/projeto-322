package Principal;
import java.util.ArrayList;
import java.util.List;

public class Procedimento implements Id_Banco{
    private int id;
    private String nome;
    private String especialidade;
    private List<Materiais> materiais;
    private CalculodeGastos gastos;
    private double preco;
    private List<Double> valoresMateriais;

    public Procedimento(String nome, String especialidade, CalculodeGastos gastos) {
        this.id=0;
        this.nome = nome;
        this.especialidade = especialidade;
        this.gastos=gastos;
        this.materiais = new ArrayList<>();
        this.valoresMateriais = new ArrayList<>();
        this.preco =calcularCustoTotal();
    }

    // Construtor para recuperar do banco de dados
    public Procedimento(int id, String nome, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.materiais = new ArrayList<>();
        this.valoresMateriais = new ArrayList<>();
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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void adicionarMaterial(Materiais m) {
        materiais.add(m);
        valoresMateriais.add(m.getValor());
        this.preco = this.calcularCustoTotal();

    }

    public void removerMaterial(Materiais m) {
        int index = materiais.indexOf(m);
        if (index >= 0) {
        materiais.remove(index);
        valoresMateriais.remove(index);
        this.preco = this.calcularCustoTotal();
    }
    }

    public void setMateriais(List<Materiais> materiais) {
        this.materiais = materiais;
    }

    public double calcularGastos(){
        double total = 0;
        for (int i = 0; i< valoresMateriais.size(); i++) {
            total += valoresMateriais.get(i);
        }
        return total+gastos.gastosTotais();
    }

    public void setGastos(CalculodeGastos gastos) {
    this.gastos = gastos;
}

    public double calcularCustoTotal() {
        double taxa;
        taxa = gastos.getTaxaServico();
        double g =calcularGastos();
        double total = taxa*(g);

        return total;
    }
    public List<Double> getValoresMateriais() {
        return valoresMateriais;
    }

    @Override
    public String toString() {
        return nome + " (" + especialidade + ") - Custo total: R$" + String.format("%.2f", calcularCustoTotal());
    }
}